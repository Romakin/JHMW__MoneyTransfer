package org.home.MoneyTransfer.dao;

public enum Currency {

    /**
     * Код RUR сделан согласно заданию.
     * НО
     * 4 августа 1997 года Президент России Б.Ельцин подписал указ №822, в соответствии с которым 1 января 1998 года
     *  правительство и Центральный Банк провели деноминацию рубля. Теперь 1 новый рубль равнялся 1000 старых рублей.
     *  Изменился и международный код рубля с RUR на RUB. Вскоре после деноминации, 17 августа 1998 года правительство
     *  объявило дефолт по внутренним  обязательствам, а курс рубля сильно упал по отношению к другим валютам. (википедия)
     */

    RUR("RUR"),
    USD("USD"),
    EUR("EUR");

    String label;
    Currency(String label) {
        this.label = label.toUpperCase();
    }

    public static Currency valueWithLabel(String label) {
        for (Currency e : values()) {
            if (e.label.equals(label.toUpperCase())) {
                return e;
            }
        }
        return null;
    }
}
