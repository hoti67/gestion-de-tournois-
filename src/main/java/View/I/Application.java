package View.I;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import Model.I.GestionBDD;
import Model.I.GestionBDD;
import java.sql.SQLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@PWA(name = "Projet Info Gestion de Tournois", shortName = "Gestion de Tournois")
@Theme("my-theme")
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        
        GestionBDD.getConnection();
        GestionBDD.dropDB();
        GestionBDD.createDB();
        GestionBDD.insertDB();
        
        SpringApplication.run(Application.class, args);
    }

}
