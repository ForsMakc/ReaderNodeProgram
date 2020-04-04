package student.bazhin.core;

import student.bazhin.components.Connector;

public class Core{

    protected View view;
    protected Storage storage;
    protected boolean working;
    protected static Core instance;

    protected Core() {}

    protected void init() {
        working = true;
        view = new View();
        storage = new Storage();
//        new Connector().perform(); //работая в другом потоке, в нём будет создаваться соединение, а также выполняться работа опрашивателя и преобразователя, проходя по списку скада
    }

    public static Core getInstance() {
        if (instance == null) {
            instance = new Core();
            instance.init();
        }
        return instance;
    }

    public View getView(){
        return view;
    }

    public Storage getStorage() {
        return storage;
    }

}
