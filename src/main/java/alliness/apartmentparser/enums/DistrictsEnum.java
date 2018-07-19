package alliness.apartmentparser.enums;

public enum DistrictsEnum {

    All(0, 0, "Все", "All"),
    Holoseevsky(1, 15184,"Голосеевский", "Holoseevsky"),
    Darnitsky(3, 15181,"Дарницкий", "Darnitsky"),
    Densyansky(5, 15183,"Деснянский", "Densyansky"),
    Dneprovsky(7, 15182,"Днепровский", "Dneprovsky"),
    Obolonsky(9, 15187,"Оболонский", "Obolonsky"),
    Pechersky(11, 15189,"Печерский", "Pechersky"),
    Podolsky(13, 15188,"Подольский", "Podolsky"),
    Svyatoshinsky(15,15186, "Святошинский", "Svyatoshinsky"),
    Solomensky(17, 15185,"Соломенский", "Solomensky"),
    Schevchenkovsky(19,15190, "Шевченковский", "Schevchenkovsky");

    public int    olxRegionId;
    public int    riaRegionId;
    public String ruName;
    public String enName;

    DistrictsEnum(int olxRegionId, int riaRegionId, String ruName, String enName) {
        this.olxRegionId = olxRegionId;
        this.riaRegionId = riaRegionId;
        this.ruName = ruName;
        this.enName = enName;
    }

}
