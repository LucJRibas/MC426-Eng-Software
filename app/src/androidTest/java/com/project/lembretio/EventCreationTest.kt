
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.common.truth.Truth.assertThat
import com.project.lembretio.EventCreator
import com.project.lembretio.EventPagerActivity
import com.project.lembretio.R
import com.project.lembretio.addeventfragments.EventTitle
import org.junit.Before
import org.junit.Rule
import org.junit.Test



//import org.mockito.Mockito.mock
//import org.mockito.Mockito.`when`

class EventCreationTest {

    private lateinit var eventCreator: EventCreator

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(EventPagerActivity::class.java)

    @Before
    fun setUp() {
        // Configurar o objeto EventCreator para simular um contexto
//        eventCreator = mock(EventCreator::class.java)
//        `when`(eventCreator.event).thenReturn(Event(isMedication = true))
    }

    @Test
    fun testNextButtonNavigationWhenTextNotEmpty() {
        // Lançar o fragmento em um contêiner simulado
        val scenario = launchFragmentInContainer<EventTitle>()

        // Verificar se o botão Next está visível
        Espresso.onView(ViewMatchers.withId(R.id.btn_title_next))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // Digitar texto no EditText
        Espresso.onView(ViewMatchers.withId(R.id.edit_text_title))
            .perform(ViewActions.typeText("Test Medication"), ViewActions.closeSoftKeyboard())

        // Clicar no botão Next
        Espresso.onView(ViewMatchers.withId(R.id.btn_title_next)).perform(ViewActions.click())

        // Verificar se a transição para a próxima tela ocorreu
        assertThat(activityScenarioRule.scenario.state).isEqualTo(Lifecycle.State.DESTROYED)
    }

    @Test
    fun testNextButtonDoesNotNavigateWhenTextIsEmpty() {
        // Lançar o fragmento em um contêiner simulado
        val scenario = launchFragmentInContainer<EventTitle>()

        // Verificar se o botão Next está visível
        Espresso.onView(ViewMatchers.withId(R.id.btn_title_next))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // Deixar o EditText vazio
        Espresso.onView(ViewMatchers.withId(R.id.edit_text_title))
            .perform(ViewActions.clearText(), ViewActions.closeSoftKeyboard())

        // Clicar no botão Next
        Espresso.onView(ViewMatchers.withId(R.id.btn_title_next)).perform(ViewActions.click())

        // Verificar se a transição para a próxima tela não ocorreu
        assertThat(activityScenarioRule.scenario.state).isEqualTo(Lifecycle.State.RESUMED)
    }

    // Adicione mais testes conforme necessário para cobrir outros aspectos do seu fragmento
}
