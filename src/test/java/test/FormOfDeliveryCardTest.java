package test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataGenerator;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import java.time.Duration;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.openqa.selenium.Keys.BACK_SPACE;

class FormOfDeliveryCardTest {

    @BeforeAll
    static void setUpAll(){
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setUpEach() {
        open("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        closeWindow();
    }

    @AfterAll
    static void tearDownAll(){
        SelenideLogger.removeListener("allure");
    }

    @Test
    void shouldSuccessfulPlanAndReplanMeeting() {
        val validUser = DataGenerator.Registration.generateUser("ru");
        int daysToAddForFirstMeeting = 4;
        int daysToAddForSecondMeeting = 7;
        $("[data-test-id='city'] .input__control").setValue(validUser.getCity());
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control")
                .sendKeys(Keys.chord(BACK_SPACE,
                        DataGenerator.generateDate(daysToAddForFirstMeeting)));
        $("[data-test-id='name'] .input__control").setValue(validUser.getName());
        $("[data-test-id='phone'] .input__control").setValue(validUser.getPhone());
        $$(".checkbox__box").find(Condition.visible).click();
        $$("button").find(Condition.exactText("Запланировать")).click();
        $(withText("Встреча успешно запланирована"))
                .shouldBe(Condition.visible, Duration.ofSeconds(15));
        $(withText("Встреча успешно запланирована"))
                .shouldBe(Condition.text(DataGenerator.generateDate(daysToAddForFirstMeeting)));
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control")
                .sendKeys(Keys.chord(BACK_SPACE,
                        DataGenerator.generateDate(daysToAddForSecondMeeting)));
        $$("button").find(Condition.exactText("Запланировать")).click();
        $(withText("Перепланировать?"))
                .shouldBe(Condition.visible, Duration.ofSeconds(15));
        $$("button").find(Condition.exactText("Перепланировать")).click();
        $(withText("Встреча успешно запланирована"))
                .shouldBe(Condition.visible, Duration.ofSeconds(15));
        $(withText("Встреча успешно запланирована"))
                .shouldBe(Condition.text(DataGenerator.generateDate(daysToAddForSecondMeeting)));
    }

    @Test
    void shouldWarnIfPlanAndReplanDateEqual() {
        val validUser = DataGenerator.Registration.generateUser("ru");
        int daysToAddForFirstMeeting = 4;
        int daysToAddForSecondMeeting = daysToAddForFirstMeeting;
        $("[data-test-id='city'] .input__control").setValue(validUser.getCity());
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control")
                .sendKeys(Keys.chord(BACK_SPACE,
                        DataGenerator.generateDate(daysToAddForFirstMeeting)));
        $("[data-test-id='name'] .input__control").setValue(validUser.getName());
        $("[data-test-id='phone'] .input__control").setValue(validUser.getPhone());
        $$(".checkbox__box").find(Condition.visible).click();
        $$("button").find(Condition.exactText("Запланировать")).click();
        $(withText("Встреча успешно запланирована"))
                .shouldBe(Condition.visible, Duration.ofSeconds(15));
        $(withText("Встреча успешно запланирована"))
                .shouldBe(Condition.text(DataGenerator.generateDate(daysToAddForFirstMeeting)));
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control")
                .sendKeys(Keys.chord(BACK_SPACE,
                        DataGenerator.generateDate(daysToAddForSecondMeeting)));
        $$("button").find(Condition.exactText("Запланировать")).click();
        $(withText("У Вас уже запланирована встреча на эту дату"))
                .shouldBe(Condition.visible, Duration.ofSeconds(15));
    }


    @Test
    void shouldTestSuccessOrderIfPlusThreeDays() {
        val validUser = DataGenerator.Registration.generateUser("ru");
        int daysToAddForFirstMeeting = 3;
        $("[data-test-id='city'] .input__control").setValue(validUser.getCity());
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control")
                .sendKeys(Keys.chord(BACK_SPACE,
                        DataGenerator.generateDate(daysToAddForFirstMeeting)));
        $("[data-test-id='name'] .input__control").setValue(validUser.getName());
        $("[data-test-id='phone'] .input__control").setValue(validUser.getPhone());
        $$(".checkbox__box").find(Condition.visible).click();
        $$("button").find(Condition.exactText("Запланировать")).click();
        $(withText("Встреча успешно запланирована"))
                .shouldBe(Condition.visible, Duration.ofSeconds(15));
        $(withText("Встреча успешно запланирована"))
                .shouldBe(Condition.text(DataGenerator.generateDate(daysToAddForFirstMeeting)));
    }


    @Test
    void shouldTestSuccessOrderIfPlus365Days() {
        val validUser = DataGenerator.Registration.generateUser("ru");
        int daysToAddForFirstMeeting = 365;
        $("[data-test-id='city'] .input__control").setValue(validUser.getCity());
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control")
                .sendKeys(Keys.chord(BACK_SPACE,
                        DataGenerator.generateDate(daysToAddForFirstMeeting)));
        $("[data-test-id='name'] .input__control").setValue(validUser.getName());
        $("[data-test-id='phone'] .input__control").setValue(validUser.getPhone());
        $$(".checkbox__box").find(Condition.visible).click();
        $$("button").find(Condition.exactText("Запланировать")).click();
        $(withText("Встреча успешно запланирована"))
                .shouldBe(Condition.visible, Duration.ofSeconds(15));
        $(withText("Встреча успешно запланирована"))
                .shouldBe(Condition.text(DataGenerator.generateDate(daysToAddForFirstMeeting)));
    }

    @Test
    void shouldTestUnsuccessOrderIfPlusTwoDays() {
        val validUser = DataGenerator.Registration.generateUser("ru");
        int daysToAddForFirstMeeting = 2;
        $("[data-test-id='city'] .input__control").setValue(validUser.getCity());
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control")
                .sendKeys(Keys.chord(BACK_SPACE,
                        DataGenerator.generateDate(daysToAddForFirstMeeting)));
        $("[data-test-id='name'] .input__control").setValue(validUser.getName());
        $("[data-test-id='phone'] .input__control").setValue(validUser.getPhone());
        $$(".checkbox__box").find(Condition.visible).click();
        $$("button").find(Condition.exactText("Запланировать")).click();
        $(withText("Заказ на выбранную дату невозможен"))
                .shouldBe(Condition.visible);
    }


    @Test
    void shouldTestUnsuccessOrderIfNoName() {
        val validUser = DataGenerator.Registration.generateUser("ru");
        int daysToAddForFirstMeeting = 3;
        $("[data-test-id='city'] .input__control").setValue(validUser.getCity());
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control")
                .sendKeys(Keys.chord(BACK_SPACE,
                        DataGenerator.generateDate(daysToAddForFirstMeeting)));
        $("[data-test-id='phone'] .input__control").setValue(validUser.getPhone());
        $$(".checkbox__box").find(Condition.visible).click();
        $$("button").find(Condition.exactText("Запланировать")).click();
        $(withText("Поле обязательно для заполнения"))
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldTestUnsuccessOrderIfNoCity() {
        val validUser = DataGenerator.Registration.generateUser("ru");
        int daysToAddForFirstMeeting = 3;
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control")
                .sendKeys(Keys.chord(BACK_SPACE,
                        DataGenerator.generateDate(daysToAddForFirstMeeting)));
        $("[data-test-id='name'] .input__control").setValue(validUser.getName());
        $("[data-test-id='phone'] .input__control").setValue(validUser.getPhone());
        $$(".checkbox__box").find(Condition.visible).click();
        $$("button").find(Condition.exactText("Запланировать")).click();
        $(withText("Поле обязательно для заполнения"))
                .shouldBe(Condition.visible);
    }


    @Test
    void shouldTestUnsuccessOrderIfNoNumber() {
        val validUser = DataGenerator.Registration.generateUser("ru");
        int daysToAddForFirstMeeting = 3;
        $("[data-test-id='city'] .input__control").setValue(validUser.getCity());
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control")
                .sendKeys(Keys.chord(BACK_SPACE,
                        DataGenerator.generateDate(daysToAddForFirstMeeting)));
        $("[data-test-id='name'] .input__control").setValue(validUser.getName());
        $$(".checkbox__box").find(Condition.visible).click();
        $$("button").find(Condition.exactText("Запланировать")).click();
        $(withText("Поле обязательно для заполнения"))
                .shouldBe(Condition.visible);
    }


    @Test
    void shouldTestUnsuccessOrderIfNoDate() {
        val validUser = DataGenerator.Registration.generateUser("ru");
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control")
                .sendKeys(Keys.chord(BACK_SPACE));
        $("[data-test-id='city'] .input__control").setValue(validUser.getCity());
        $("[data-test-id='name'] .input__control").setValue(validUser.getName());
        $("[data-test-id='phone'] .input__control").setValue(validUser.getPhone());
        $$(".checkbox__box").find(Condition.visible).click();
        $$("button").find(Condition.exactText("Запланировать")).click();
        $(withText("Неверно введена дата"))
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldTestUnsuccessOrderIfCityNotFromList() {
        val invalidUser = DataGenerator.InvalidRegistration.generateInvalidUser("ru");
        int daysToAddForFirstMeeting = 3;
        $("[data-test-id='city'] .input__control").setValue(invalidUser.getCity());
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control")
                .sendKeys(Keys.chord(BACK_SPACE,
                        DataGenerator.generateDate(daysToAddForFirstMeeting)));
        $("[data-test-id='name'] .input__control").setValue(invalidUser.getName());
        $("[data-test-id='phone'] .input__control").setValue(invalidUser.getPhone());
        $$(".checkbox__box").find(Condition.visible).click();
        $$("button").find(Condition.exactText("Запланировать")).click();
        $(withText("Доставка в выбранный город недоступна"))
                .shouldBe(Condition.visible);
    }


    @Test
    void shouldTestUnsuccessOrderIfNameInEnglish() {
        val invalidUser = DataGenerator.InvalidNameRegistration.generateInvalidUser("ru");
        int daysToAddForFirstMeeting = 3;
        $("[data-test-id='city'] .input__control").setValue(invalidUser.getCity());
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control")
                .sendKeys(Keys.chord(BACK_SPACE,
                        DataGenerator.generateDate(daysToAddForFirstMeeting)));
        $("[data-test-id='name'] .input__control").setValue(invalidUser.getName());
        $("[data-test-id='phone'] .input__control").setValue(invalidUser.getPhone());
        $$(".checkbox__box").find(Condition.visible).click();
        $$("button").find(Condition.exactText("Запланировать")).click();
        $(withText("только русские буквы, пробелы и дефисы"))
                .shouldBe(Condition.visible);
    }


    @Test
    void shouldTestUnsuccessOrderIfTelNotContainsElevenFigures() {
        val invalidUser = DataGenerator.InvalidRegistrationPhone.generateInvalidUser("ru");
        int daysToAddForFirstMeeting = 3;
        $("[data-test-id='city'] .input__control").setValue(invalidUser.getCity());
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control")
                .sendKeys(Keys.chord(BACK_SPACE,
                        DataGenerator.generateDate(daysToAddForFirstMeeting)));
        $("[data-test-id='name'] .input__control").setValue(invalidUser.getName());
        $("[data-test-id='phone'] .input__control").setValue(invalidUser.getPhone());
        $$(".checkbox__box").find(Condition.visible).click();
        $$("button").find(Condition.exactText("Запланировать")).click();
        $(withText("Телефон указан неверно"))
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldTestUnsuccessOrderIfTelNotStartWithPlusSeven() {
        val invalidUser = DataGenerator.InvalidRegistrationPhoneStarts.generateInvalidUser("ru");
        int daysToAddForFirstMeeting = 3;
        $("[data-test-id='city'] .input__control").setValue(invalidUser.getCity());
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control")
                .sendKeys(Keys.chord(BACK_SPACE,
                        DataGenerator.generateDate(daysToAddForFirstMeeting)));
        $("[data-test-id='name'] .input__control").setValue(invalidUser.getName());
        $("[data-test-id='phone'] .input__control").setValue(invalidUser.getPhone());
        $$(".checkbox__box").find(Condition.visible).click();
        $$("button").find(Condition.exactText("Запланировать")).click();
        $(withText("Телефон указан неверно"))
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldTestUnsuccessOrderIfNoClickOnCofirmation() {
        val validUser = DataGenerator.Registration.generateUser("ru");
        int daysToAddForFirstMeeting = 3;
        $("[data-test-id='city'] .input__control").setValue(validUser.getCity());
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control")
                .sendKeys(Keys.chord(BACK_SPACE,
                        DataGenerator.generateDate(daysToAddForFirstMeeting)));
        $("[data-test-id='name'] .input__control").setValue(validUser.getName());
        $("[data-test-id='phone'] .input__control").setValue(validUser.getPhone());
        $$("button").find(Condition.exactText("Запланировать")).click();
        $$("button").find(Condition.exactText("Запланировать")).click();
        String text = $(".checkbox__text").getCssValue("color");
        assertEquals("rgba(255, 92, 92, 1)", text);
    }

    @Test
    void shouldTestSuccessOrderIfNameWithSpaceAndHyphen() {
        val validUser = DataGenerator.Registration.generateUser("ru");
        int daysToAddForFirstMeeting = 3;
        $("[data-test-id='city'] .input__control").setValue(validUser.getCity());
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control")
                .sendKeys(Keys.chord(BACK_SPACE,
                        DataGenerator.generateDate(daysToAddForFirstMeeting)));
        $("[data-test-id='name'] .input__control").setValue("Гребенькова-Ушакова Мария-Анжела");
        $("[data-test-id='phone'] .input__control").setValue(validUser.getPhone());
        $$(".checkbox__box").find(Condition.visible).click();
        $$("button").find(Condition.exactText("Запланировать")).click();
        $(withText("Встреча успешно запланирована"))
                .shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestSuccessOrderIfNameWithLetterYo() {
        val invalidUser = DataGenerator.InvalidNameRegistrationWithYo.generateInvalidUser("ru");
        int daysToAddForFirstMeeting = 3;
        $("[data-test-id='city'] .input__control").setValue(invalidUser.getCity());
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control")
                .sendKeys(Keys.chord(BACK_SPACE,
                        DataGenerator.generateDate(daysToAddForFirstMeeting)));
        $("[data-test-id='name'] .input__control").setValue(invalidUser.getName());
        $("[data-test-id='phone'] .input__control").setValue(invalidUser.getPhone());
        $$(".checkbox__box").find(Condition.visible).click();
        $$("button").find(Condition.exactText("Запланировать")).click();
        $(withText("Встреча успешно запланирована"))
                .shouldBe(Condition.visible, Duration.ofSeconds(15));
    }
}
