package smartfarming.entites;


import java.time.LocalDate;
import smartfarming.enums.StatutRecolte;
import smartfarming.enums.TypeCulture;

public class Culture {

    private String      id;
    private TypeCulture type;
    private LocalDate   datePlantation;
    private LocalDate   dateRecoltePrevue;
    private StatutRecolte stadeCroissance;

    private double phMin;
    private double phMax;
    private double humiditeMin;
    private double humiditeMax;

    public Culture(String id, TypeCulture
 type,
                   LocalDate datePlantation, LocalDate dateRecoltePrevue,
                   double phMin, double phMax,
                   double humiditeMin, double humiditeMax) {
        this.id                = id;
        this.type              = type;
        this.datePlantation    = datePlantation;
        this.dateRecoltePrevue = dateRecoltePrevue;
        this.stadeCroissance   = StatutRecolte.SEMIS;
        this.phMin             = phMin;
        this.phMax             = phMax;
        this.humiditeMin       = humiditeMin;
        this.humiditeMax       = humiditeMax;
    }

    public String        getId()               { return id; }
    public TypeCulture
      getType()             { return type; }
    public LocalDate     getDatePlantation()   { return datePlantation; }
    public LocalDate     getDateRecoltePrevue(){ return dateRecoltePrevue; }
    public StatutRecolte getStadeCroissance()  { return stadeCroissance; }
    public double        getPhMin()            { return phMin; }
    public double        getPhMax()            { return phMax; }
    public double        getHumiditeMin()      { return humiditeMin; }
    public double        getHumiditeMax()      { return humiditeMax; }

    public void setDateRecoltePrevue(LocalDate date) { this.dateRecoltePrevue = date; }
    public void setPhMin(double phMin)               { this.phMin = phMin; }
    public void setPhMax(double phMax)               { this.phMax = phMax; }
    public void setHumiditeMin(double h)             { this.humiditeMin = h; }
    public void setHumiditeMax(double h)             { this.humiditeMax = h; }

    public void mettreAJourStade(StatutRecolte stade) {
        this.stadeCroissance = stade;
        System.out.println("Culture [" + id + "] (" + type + ") - Stade mis a jour : " + stade);
    }

    @Override
    public String toString() {
        return "Culture{id='" + id + "', type=" + type +
               ", stade=" + stadeCroissance +
               ", plantation=" + datePlantation +
             ", recoltePrevue=" + dateRecoltePrevue +
               ", pH=[" + phMin + "-" + phMax + "]" +
             ", humidite=[" + humiditeMin + "-" + humiditeMax + "]}";
    }
}
