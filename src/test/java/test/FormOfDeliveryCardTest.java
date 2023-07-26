package test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataGenerator;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.openqa.selenium.Keys.BACK_SPACE;

class FormOfDeliveryCardTest {

    @BeforeAll
    static void setUpAll() {
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
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    void shouldSuccessfulPlanAndReplanMeeting() {
        val validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE, firstMeetingDate);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Запланировать')]").click();
        $(".notification__content").shouldHave(text("Встреча успешно запланирована на " + firstMeetingDate)).shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE, firstMeetingDate);
        $x("//*[contains(text(),'Запланировать')]").click();
        $x("//span[contains(text(),'Перепланировать')]").shouldBe(appear, Duration.ofSeconds(3)).click();
        $(".notification__content").shouldHave(text("Встреча успешно запланирована на " + firstMeetingDate)).shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE, secondMeetingDate);
        $x("//*[contains(text(),'Запланировать')]").click();
        $x("//span[contains(text(),'Перепланировать')]").click();
        $(".notification__content").shouldHave(text("Встреча успешно запланирована на " + secondMeetingDate)).shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestSuccessOrderIfPlusThreeDays() {
        val validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 3;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE, firstMeetingDate);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Запланировать')]").click();
        $(".notification__content").shouldHave(text("Встреча успешно запланирована на " + firstMeetingDate)).shouldBe(Condition.visible, Duration.ofSeconds(15));
    }


    @Test
    void shouldTestSuccessOrderIfPlus365Days() {
        val validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 365;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE, firstMeetingDate);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Запланировать')]").click();
        $(".notification__content").shouldHave(text("Встреча успешно запланирована на " + firstMeetingDate)).shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestUnsuccessOrderIfPlusTwoDays() {
        val validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 2;
        $("[data-test-id='city'] .input__control").setValue(validUser.getCity());
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control")
                .sendKeys(Keys.chord(BACK_SPACE,
                        DataGenerator.generateDate(daysToAddForFirstMeeting)));
        $("[data-test-id='name'] .input__control").setValue(validUser.getName());
        $("[data-test-id='phone'] .input__control").setValue(validUser.getPhone());
        $$(".checkbox__box").find(Condition.visible).click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id='date'] .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldTestUnsuccessOrderIfNoName() {
        val validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 3;
        $("[data-test-id='city'] .input__control").setValue(validUser.getCity());
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control")
                .sendKeys(Keys.chord(BACK_SPACE,
                        DataGenerator.generateDate(daysToAddForFirstMeeting)));
        $("[data-test-id='phone'] .input__control").setValue(validUser.getPhone());
        $$(".checkbox__box").find(Condition.visible).click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id='name'] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldTestUnsuccessOrderIfNoCity() {
        val validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 3;
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control")
                .sendKeys(Keys.chord(BACK_SPACE,
                        DataGenerator.generateDate(daysToAddForFirstMeeting)));
        $("[data-test-id='name'] .input__control").setValue(validUser.getName());
        $("[data-test-id='phone'] .input__control").setValue(validUser.getPhone());
        $$(".checkbox__box").find(Condition.visible).click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id='city'] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldTestUnsuccessOrderIfNoNumber() {
        val validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 3;
        $("[data-test-id='city'] .input__control").setValue(validUser.getCity());
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control")
                .sendKeys(Keys.chord(BACK_SPACE,
                        DataGenerator.generateDate(daysToAddForFirstMeeting)));
        $("[data-test-id='name'] .input__control").setValue(validUser.getName());
        $$(".checkbox__box").find(Condition.visible).click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id='phone'] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
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
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id='date'] .input__sub").shouldHave(exactText("Неверно введена дата"));
    }

    @Test
    void shouldTestUnsuccessOrderIfCityNotFromList() {
        val invalidUser = DataGenerator.InvalidRegistration.generateInvalidUser("ru");
        var daysToAddForFirstMeeting = 3;
        $("[data-test-id='city'] .input__control").setValue(invalidUser.getCity());
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control")
                .sendKeys(Keys.chord(BACK_SPACE,
                        DataGenerator.generateDate(daysToAddForFirstMeeting)));
        $("[data-test-id='name'] .input__control").setValue(invalidUser.getName());
        $("[data-test-id='phone'] .input__control").setValue(invalidUser.getPhone());
        $$(".checkbox__box").find(Condition.visible).click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id='city'] .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldTestUnsuccessOrderIfNameInEnglish() {
        val invalidUser = DataGenerator.InvalidNameRegistration.generateInvalidUser("ru");
        var daysToAddForFirstMeeting = 3;
        $("[data-test-id='city'] .input__control").setValue(invalidUser.getCity());
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control")
                .sendKeys(Keys.chord(BACK_SPACE,
                        DataGenerator.generateDate(daysToAddForFirstMeeting)));
        $("[data-test-id='name'] .input__control").setValue(invalidUser.getName());
        $("[data-test-id='phone'] .input__control").setValue(invalidUser.getPhone());
        $$(".checkbox__box").find(Condition.visible).click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id='name'] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldTestUnsuccessOrderIfNoClickOnCofirmation() {
        val validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 3;
        $("[data-test-id='city'] .input__control").setValue(validUser.getCity());
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control")
                .sendKeys(Keys.chord(BACK_SPACE,
                        DataGenerator.generateDate(daysToAddForFirstMeeting)));
        $("[data-test-id='name'] .input__control").setValue(validUser.getName());
        $("[data-test-id='phone'] .input__control").setValue(validUser.getPhone());
        $$("button").find(exactText("Запланировать")).click();
        $$(".input__invalid");
    }

    @Test
    void shouldTestSuccessOrderIfNameWithSpaceAndHyphen() {
        val validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 3;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        $("[data-test-id='city'] .input__control").setValue(validUser.getCity());
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control").sendKeys(Keys.chord(BACK_SPACE, firstMeetingDate));
        $("[data-test-id='name'] .input__control").setValue("Гребенькова-Ушакова Мария-Анжела");
        $("[data-test-id='phone'] .input__control").setValue(validUser.getPhone());
        $$(".checkbox__box").find(Condition.visible).click();
        $$("button").find(exactText("Запланировать")).click();
        $(".notification__content").shouldHave(text("Встреча успешно запланирована на " + firstMeetingDate)).shouldBe(Condition.visible, Duration.ofSeconds(15));
    }
}
