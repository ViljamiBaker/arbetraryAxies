package axies.objects.logic;

// runs a runnable once when activated
public class GenericLogicOBJ extends LogicObject{

    private Runnable codeToRun;

    public GenericLogicOBJ(String tag, Runnable codeToRun){
        super(tag, "");
        this.codeToRun = codeToRun;
    }

    @Override
    public void activate(){
        codeToRun.run();
    }

    @Override
    public void deactivate(){}
}
