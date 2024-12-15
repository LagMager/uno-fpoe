package org.example.eiscuno.model.unoenum;

/**
 * Enum EISCUnoEnum
 *
 * This enum represents the various file paths for the images used in the EISC Uno game.
 */
public enum EISCUnoEnum {
    FAVICON("favicon.png", null),
    UNO("images/uno.png", null),
    BACKGROUND_UNO("images/background_uno.png", null),
    BUTTON_UNO("images/button_uno.png", null),
    CARD_UNO("cards-uno/card_uno.png", null),
    DECK_OF_CARDS("cards-uno/deck_of_cards.png", null),
    WILD("cards-uno/wild.png", "WILD"),
    TWO_WILD_DRAW_BLUE("cards-uno/2_wild_draw_blue.png", "DRAW_TWO"),
    TWO_WILD_DRAW_GREEN("cards-uno/2_wild_draw_green.png", "DRAW_TWO"),
    TWO_WILD_DRAW_YELLOW("cards-uno/2_wild_draw_yellow.png", "DRAW_TWO"),
    TWO_WILD_DRAW_RED("cards-uno/2_wild_draw_red.png", "DRAW_TWO"),
    FOUR_WILD_DRAW("cards-uno/4_wild_draw.png", "WILD_DRAW_FOUR"),
    SKIP_BLUE("cards-uno/skip_blue.png", "SKIP"),
    SKIP_YELLOW("cards-uno/skip_yellow.png", "SKIP"),
    SKIP_GREEN("cards-uno/skip_green.png", "SKIP"),
    SKIP_RED("cards-uno/skip_red.png", "SKIP"),
    REVERSE_BLUE("cards-uno/reserve_blue.png", "REVERSE"),
    REVERSE_YELLOW("cards-uno/reserve_yellow.png", "REVERSE"),
    REVERSE_GREEN("cards-uno/reserve_green.png", "REVERSE"),
    REVERSE_RED("cards-uno/reserve_red.png", "REVERSE"),
    GREEN_0("cards-uno/0_green.png", "NUMBER"),
    GREEN_1("cards-uno/1_green.png", "NUMBER"),
    GREEN_2("cards-uno/2_green.png", "NUMBER"),
    GREEN_3("cards-uno/3_green.png", "NUMBER"),
    GREEN_4("cards-uno/4_green.png", "NUMBER"),
    GREEN_5("cards-uno/5_green.png", "NUMBER"),
    GREEN_6("cards-uno/6_green.png", "NUMBER"),
    GREEN_7("cards-uno/7_green.png", "NUMBER"),
    GREEN_8("cards-uno/8_green.png", "NUMBER"),
    GREEN_9("cards-uno/9_green.png", "NUMBER"),
    YELLOW_0("cards-uno/0_yellow.png", "NUMBER"),
    YELLOW_1("cards-uno/1_yellow.png", "NUMBER"),
    YELLOW_2("cards-uno/2_yellow.png", "NUMBER"),
    YELLOW_3("cards-uno/3_yellow.png", "NUMBER"),
    YELLOW_4("cards-uno/4_yellow.png", "NUMBER"),
    YELLOW_5("cards-uno/5_yellow.png", "NUMBER"),
    YELLOW_6("cards-uno/6_yellow.png", "NUMBER"),
    YELLOW_7("cards-uno/7_yellow.png", "NUMBER"),
    YELLOW_8("cards-uno/8_yellow.png", "NUMBER"),
    YELLOW_9("cards-uno/9_yellow.png", "NUMBER"),
    BLUE_0("cards-uno/0_blue.png", "NUMBER"),
    BLUE_1("cards-uno/1_blue.png", "NUMBER"),
    BLUE_2("cards-uno/2_blue.png", "NUMBER"),
    BLUE_3("cards-uno/3_blue.png", "NUMBER"),
    BLUE_4("cards-uno/4_blue.png", "NUMBER"),
    BLUE_5("cards-uno/5_blue.png", "NUMBER"),
    BLUE_6("cards-uno/6_blue.png", "NUMBER"),
    BLUE_7("cards-uno/7_blue.png", "NUMBER"),
    BLUE_8("cards-uno/8_blue.png", "NUMBER"),
    BLUE_9("cards-uno/9_blue.png", "NUMBER"),
    RED_0("cards-uno/0_red.png", "NUMBER"),
    RED_1("cards-uno/1_red.png", "NUMBER"),
    RED_2("cards-uno/2_red.png", "NUMBER"),
    RED_3("cards-uno/3_red.png", "NUMBER"),
    RED_4("cards-uno/4_red.png", "NUMBER"),
    RED_5("cards-uno/5_red.png", "NUMBER"),
    RED_6("cards-uno/6_red.png", "NUMBER"),
    RED_7("cards-uno/7_red.png", "NUMBER"),
    RED_8("cards-uno/8_red.png", "NUMBER"),
    RED_9("cards-uno/9_red.png", "NUMBER");


    private final String filePath;
    private final String cardType;
    private static final String PATH = "/org/example/eiscuno/";

    /**
     * Constructor for the EISCUnoEnum enum.
     *
     * @param filePath the file path of the image relative to the base directory
     */
    EISCUnoEnum(String filePath, String cardType) {
        this.filePath = PATH + filePath;
        this.cardType = cardType;
    }

    /**
     * Gets the full file path of the image.
     *
     * @return the full file path of the image
     */
    public String getFilePath() {
        return filePath;
    }

    public String getCardType() {return cardType;}

    public boolean isPlayableCard(){
        return cardType != null;
    }
}
