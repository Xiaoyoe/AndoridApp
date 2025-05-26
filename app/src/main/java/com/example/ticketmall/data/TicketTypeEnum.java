package com.example.ticketmall.data;

public enum TicketTypeEnum {
    ALL(null, "全部"),
    MOVIE(1, "电影"),
    CONCERT(2, "演唱会"),
    MUSIC(3, "音乐节"),
    COMEDY(4, "脱口秀");

    private final Integer code;

    private final String name;

    TicketTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static TicketTypeEnum getByCode(Integer code) {
        for (TicketTypeEnum ticketTypeEnum : TicketTypeEnum.values()) {
            if (ticketTypeEnum.getCode().equals(code)) {
                return ticketTypeEnum;
            }
        }
        return null;
    }

    public static TicketTypeEnum getByName(String name) {
        for (TicketTypeEnum ticketTypeEnum : TicketTypeEnum.values()) {
            if (ticketTypeEnum.getName().equals(name)) {
                return ticketTypeEnum;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
