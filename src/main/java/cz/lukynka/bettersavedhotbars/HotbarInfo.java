package cz.lukynka.bettersavedhotbars;

public class HotbarInfo {

    public HotbarInfo(Integer slot, Integer row) {
        this.slot = slot;
        this.row = row;
    }

    public Integer getSlot() {
        return slot;
    }

    public Integer getRow() {
        return row;
    }

    Integer slot;
    Integer row;
}
