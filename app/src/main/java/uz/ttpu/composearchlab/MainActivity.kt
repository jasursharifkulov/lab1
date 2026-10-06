package uz.ttpu.composearchlab

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.ttpu.composearchlab.ui.theme.ComposeArchLabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeArchLabTheme {
                Scaffold { innerPadding ->
                    // Вы можете переключать экраны для проверки здесь:
                    // 1. Для проверки Task 2 и Task 3 (Поле ввода имени):
                    NameScreen(modifier = Modifier.padding(innerPadding))

                    // 2. Для проверки Task 4 (Эксперимент с рекомпозицией в Logcat):
                    // NewsScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// ==========================================
// TASKS 2 & 3 — STATE & STATE HOISTING
// ==========================================

// Stateful-компонент: владеет состоянием и передает его вниз
@Composable
fun NameScreen(modifier: Modifier = Modifier) {
    // КОММЕНТАРИЙ TASK 2: rememberSaveable сохраняет состояние в Bundle,
    // поэтому данные выживают при уничтожении Activity (например, при повороте экрана).
    var name by rememberSaveable { mutableStateOf("") }

    Column(modifier.padding(24.dp)) {
        NameField(
            name = name,
            onNameChange = { name = it },
            modifier = modifier
        )
        Text(text = "Hello, $name!")
    }
}

// Stateless-компонент: не имеет своего состояния, легко тестируется и переиспользуется
@Composable
fun NameField(
    name: String,
    onNameChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = name,
        onValueChange = onNameChange,
        label = { Text("Name") },
        modifier = modifier
    )
}

// Просмотр компонента без запуска эмулятора (Task 3)
@Preview(showBackground = true)
@Composable
fun NameFieldPreview() {
    ComposeArchLabTheme {
        NameField(name = "Amin", onNameChange = {})
    }
}

// ==========================================
// TASK 4 — GIVE COMPOSABLES ONLY WHAT THEY NEED
// ==========================================

data class News(val title: String, val subtitle: String, val views: Int)

@Composable
fun Header(news: News) {
    SideEffect { Log.d("Recompose", "Header(news)") }
    Column {
        Text(text = news.title)
        Text(text = news.subtitle)
    }
}

// КОММЕНТАРИЙ TASK 4 (Дизайн): Этот Header лучше, так как он принимает только примитивы.
// Он не будет перерисовываться (рекомпоноваться), если изменятся другие поля класса News (например, views).
@Composable
fun Header(title: String, subtitle: String) {
    SideEffect { Log.d("Recompose", "Header(title, subtitle)") }
    Column {
        Text(text = title)
        Text(text = subtitle)
    }
}

@Composable
fun NewsScreen(modifier: Modifier = Modifier) {
    var news by remember {
        mutableStateOf(News("Compose is declarative", "State in, UI out", views = 0))
    }

    // КОММЕНТАРИЙ TASK 4 (Группировка): Параметры стоит объединять в класс, когда они
    // логически неразделимы (например, Point(x, y)) или когда их становится слишком много (более 4-5),
    // чтобы не перегружать сигнатуру функции.
    Column(modifier.padding(24.dp)) {
        Header(news = news)
        Header(title = news.title, subtitle = news.subtitle)
        Text(text = "Views: ${news.views}")
        Button(onClick = { news = news.copy(views = news.views + 1) }) {
            Text(text = "Add view")
        }
    }
}
