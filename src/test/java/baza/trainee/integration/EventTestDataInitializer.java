package baza.trainee.integration;

import baza.trainee.domain.enums.BlockType;
import baza.trainee.domain.model.ContentBlock;
import baza.trainee.domain.model.Event;
import baza.trainee.repository.EventRepository;
import lombok.Getter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/**
 * Initialize {@link Event} records to Redis DB for integration tests.
 *
 * @author Evhen Malysh
 */
@TestConfiguration
class EventTestDataInitializer {

    private static final int EVENT_COUNT = 20;
    private static final int CONTENT_BLOCKS_COUNT = 5;

    @Bean
    CommandLineRunner testDataInitializer(EventRepository repository) {
        return args -> {
            if (!repository.findAll().isEmpty()) {
                repository.deleteAll();
            }

            IntStream.range(0, EVENT_COUNT).forEach(i -> {
                var event = new Event();

                String title = EventTitles.values()[i].getValue();
                event.setTitle(title);

                String description = EventDescription.values()[i].getValue();
                event.setDescription(description);

                EventType[] types = EventType.values();
                int typeIndex = ThreadLocalRandom.current().nextInt(types.length);
                event.setType(types[typeIndex].getValue());

                event.setBannerPreviewURI("null");
                event.setBannerURI("null");

                LocalDate begin = LocalDate.now().plusDays(i);
                event.setBegin(begin);
                event.setEnd(begin.plusDays(i));

                event.addTag("null");

                IntStream.range(0, CONTENT_BLOCKS_COUNT).forEach(j -> {
                    var block = new ContentBlock();

                    block.setOrder(j);

                    var type = (j % 2 == 0) ? BlockType.PICTURE_BLOCK
                            : BlockType.PICTURE_TEXT_BLOCK;
                    block.setBlockType(type);

                    int column = ThreadLocalRandom.current().nextInt(1) + 1;
                    block.setColumns(column);

                    block.setPictureLink("https://example.com/api/image/" + j);
                    block.setTextContent("Унікальний контент " + i + " - " + j);

                    event.addContentBlock(block);
                });

                repository.save(event);
            });
        };
    }

    @Getter
    private enum EventType {
        TYPE_1("Виставка"),
        TYPE_2("Лекція"),
        TYPE_3("Фотовиставка"),
        TYPE_4("Форум");

        private final String value;

        EventType(String value) {
            this.value = value;
        }
    }

    @Getter
    private enum EventTitles {
        EVENT_1("Виставка архітектурних творінь"),
        EVENT_2("Лекція про життя і творчість Кавалерідзе"),
        EVENT_3("Архітектурна екскурсія"),
        EVENT_4("Майстер-клас із малювання архітектурних макетів"),
        EVENT_5("Показ фільмів про архітектуру"),
        EVENT_6("Панельна дискусія: Архітектура майбутнього"),
        EVENT_7("Фотовиставка: Кавалерідзе і архітектурна спадщина"),
        EVENT_8("Архітектура та історія: Діалог із експертами"),
        EVENT_9("Архітектура і природа: Взаємозв'язок"),
        EVENT_10("Конкурс архітектурного дизайну"),
        EVENT_11("Архітектура та сталість: Нові підходи"),
        EVENT_12("Архітектура в поп-культурі: Виставка та обговорення"),
        EVENT_13("Філософія архітектури: Лекція і дискусія"),
        EVENT_14("Форум з міського планування: Майбутнє міст"),
        EVENT_15("Фотографія архітектурного силуету міста"),
        EVENT_16("Архітектура та світло: Творчість Кавалерідзе"),
        EVENT_17("Виставка архітектурних матеріалів"),
        EVENT_18("Панельна дискусія: Історія архітектури"),
        EVENT_19("Архітектура та спільнота: Вплив на життя містян"),
        EVENT_20("Архітектура і технології: Інновації в будівництві");

        private final String value;

        EventTitles(String value) {
            this.value = value;
        }

    }

    @Getter
    public enum EventDescription {
        EVENT_1("На цій події будуть представлені найкращі архітектурні творіння."),
        EVENT_2("Дізнайтеся більше про життя і внесок Кавалерідзе в архітектуру."),
        EVENT_3("Приєднуйтесь до нас для захоплюючої екскурсії містом і його архітектурою."),
        EVENT_4("Опануйте навички малювання архітектурних макетів під керівництвом професіоналів."),
        EVENT_5("Перегляньте цікаві фільми про архітектуру та дизайн."),
        EVENT_6("Долучайтеся до обговорення майбутнього архітектурного дизайну."),
        EVENT_7("Відвідайте фотовиставку, присвячену спадщині Кавалерідзе."),
        EVENT_8("Проведіть час в інтелектуальному діалозі про архітектуру та історію."),
        EVENT_9("Дізнайтеся, як архітектура взаємодіє з природою та довкіллям."),
        EVENT_10("Прийміть участь у конкурсі архітектурного дизайну і покажіть свої таланти."),
        EVENT_11("Дізнайтеся про інноваційні підходи до сталого будівництва."),
        EVENT_12("Відвідайте виставку та обговорення архітектури в поп-культурі."),
        EVENT_13("Глибше розберіться в філософії архітектури на лекції та дискусії."),
        EVENT_14("Обговорюйте майбутнє планування міст на форумі."),
        EVENT_15("Запечатліть красу архітектурного силуету міста на фотографіях."),
        EVENT_16("Оцініть творчість Кавалерідзе в контексті архітектури та світла."),
        EVENT_17("Дізнайтеся більше про матеріали, використовувані в архітектурі."),
        EVENT_18("Глибше розберіться в історії архітектури на дискусії."),
        EVENT_19("Обговоріть вплив архітектури на життя місцевої спільноти."),
        EVENT_20("Дізнайтеся про останні інновації у будівництві та архітектурі.");

        private final String value;

        EventDescription(String description) {
            this.value = description;
        }
    }

}
