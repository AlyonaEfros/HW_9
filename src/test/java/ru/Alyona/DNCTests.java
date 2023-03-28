package ru.Alyona;

import com.codeborne.selenide.CollectionCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class DNCTests {

    @BeforeEach
    void setup() {
        open("https://www.dns-shop.ru/");
    }

    @ValueSource(strings = {"Смартфон", "Телевизор"})
    @ParameterizedTest(name = "В поиске на сайте ДНС отображается не меньше 18 результатов по запросу {0}")
    @Tags({@Tag("Major"), @Tag("WEB")})

    void searchResultsShouldBeGreaterThan10(String testData){
        $(".presearch__input").setValue(testData).pressEnter();
        $(".products-list ").$$("[data-id=product]").shouldHave(sizeGreaterThanOrEqual(18));
    }

    @CsvSource(value = {"Смартфон,         Смартфон Apple iPhone 13 128 ГБ черный",
                        "Телевизор,        (139 см) Телевизор LED Xiaomi MI TV P1 55 черный"})
    @ParameterizedTest(name = "В первом результате выдачи для {0} должен отображаться текст {1}")
    @Tags({@Tag("Major"), @Tag("WEB")})

    void firstSearchResultsShouldContainExpectedText(String testData, String expectedText){
        $(".presearch__input").setValue(testData).pressEnter();
        $(".products-list ").$$("[data-id=product]").first().shouldHave(text(expectedText));
    }

    static Stream<Arguments> displayingListOfSubcategory() {
        return Stream.of(
                Arguments.of("Бытовая техника", List.of("Встраиваемая техника", "Техника для кухни", "Техника для дома")),
                Arguments.of("Красота и здоровье", List.of("Укладка и сушка волос", "Бритье, стрижка и эпиляция", "Уход за полостью рта", "Уход за телом"))
        );
    }

    @MethodSource()
    @ParameterizedTest(name = "При переходе в категорию {0}, отображается  подкатегория {1}")
    @Tags({@Tag("Critical"), @Tag("WEB")})

    void displayingListOfSubcategory(String testData, List<String> expectedButtons){
        $(".header-bottom__catalog-spoiler").click();
        $(".catalog-menu-rootmenu").$(byText(testData)).click();
        $$(".subcategory__item-container a").shouldHave(CollectionCondition.texts(expectedButtons));
    }
}

