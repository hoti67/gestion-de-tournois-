/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.I;

import Model.I.User;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route(value = "Home", layout = Layout.class)
public class HomeView extends VerticalLayout{
    
    public HomeView(){
        // Récupérer l'utilisateur actuel
        User currentUser = VaadinSession.getCurrent().getAttribute(User.class);

        // Afficher une notification avec l'e-mail et le rôle
        if (currentUser.getRole() == 0){
            Notification.show(currentUser.getEmail() + " : Admin");
        } else Notification.show(currentUser.getEmail() + " : Joueurs");
        
        H1 title = new H1("Bienvenue sur MoveOn");
        title.getStyle().set("width", "100%");
        title.getElement().getStyle().set("text-align", "center");
        
        
        // Créer les paragraphes en utilisant le composant Paragraph
        H3 T2 = new H3("Votre plateforme de mobilité internationale à portée de main");

        Paragraph p2 = new Paragraph("Félicitations, vous êtes désormais connecté à MoveOn, la plateforme dédiée aux étudiants de l'INSA Strasbourg qui souhaitent partir en mobilité à l'étranger. Que vous soyez en quête d’un semestre d’échange académique, d’un stage dans une entreprise internationale, ou même d’une expérience de volontariat, MoveOn est là pour vous offrir un accès direct à une multitude d’opportunités de mobilité dans le monde entier.");
        p2.getStyle()
            .set("text-align", "justify")
            .set("width", "100%");
        
        H3 T3 = new H3("Explorez vos options de mobilité");

        Paragraph p3 = new Paragraph("MoveOn centralise toutes les informations dont vous avez besoin pour concrétiser votre projet à l'international. Vous pouvez consulter les destinations partenaires, découvrir les programmes académiques disponibles, et trouver des offres de stage dans des entreprises internationales. Chaque programme est détaillé pour vous permettre de choisir celui qui correspond le mieux à vos objectifs académiques et professionnels.");
        p3.getStyle()
            .set("text-align", "justify")
            .set("width", "100%");
        
        H3 T4 = new H3("Nos partenaires à travers le monde");

        Paragraph p4 = new Paragraph("MoveOn collabore avec un réseau étendu d'universités, d'entreprises et d’organisations partenaires à travers le monde. Ces partenariats offrent une multitude d'opportunités d'échanges académiques et professionnels, allant des universités européennes aux institutions en Asie, en Amérique et en Afrique. Vous avez ainsi la possibilité de choisir parmi un large éventail de destinations et de programmes adaptés à vos besoins et à vos envies.");
        p4.getStyle()
            .set("text-align", "justify")
            .set("width", "100%");
        
        H3 T5 = new H3("Suivez l'avancement de votre projet");

        Paragraph p5 = new Paragraph("Sur votre espace personnel, vous avez un accès direct à toutes les informations concernant votre projet de mobilité. Vous pouvez suivre l'état de vos candidatures, consulter les documents nécessaires à votre inscription, et vérifier les dates importantes. MoveOn vous permet de gérer facilement toutes les démarches administratives liées à votre mobilité, de la candidature à l’obtention du visa, en passant par la recherche de logement.");
        p5.getStyle()
            .set("text-align", "justify")
            .set("width", "100%");
        
        H3 T6 = new H3("Témoignages d'étudiants");

        Paragraph p6 = new Paragraph("\"Mon semestre à Barcelone a été une expérience inoubliable. Non seulement j’ai pu découvrir une nouvelle culture, mais j'ai aussi amélioré mon espagnol et élargi mon réseau professionnel. MoveOn m’a permis de trouver une destination qui correspondait parfaitement à mes attentes.\" — Marie, étudiante en génie civil");
        p6.getStyle()
            .set("text-align", "justify")
            .set("width", "100%");
        
        Paragraph p7 = new Paragraph("\"Grâce à MoveOn, j'ai trouvé un stage en Allemagne qui m'a permis de travailler sur des projets innovants. Cette expérience m’a donné une nouvelle perspective sur mon domaine d’études et m’a ouvert de nombreuses portes pour ma carrière.\" — Paul, étudiant en informatique");
        p7.getStyle()
            .set("text-align", "justify")
            .set("width", "100%");
        
        Paragraph p8 = new Paragraph("\"Partir à l’étranger pour un semestre m’a permis de vivre une expérience académique et personnelle enrichissante. Le site MoveOn m’a offert une interface simple et efficace pour gérer toutes les étapes de mon départ.\" — Clara, étudiante en architecture");
        p8.getStyle()
            .set("text-align", "justify")
            .set("width", "100%");
        
        H3 T7 = new H3("Prêt à partir ?");

        Paragraph p9 = new Paragraph("Il est maintenant temps de concrétiser votre projet de mobilité. Explorez dès maintenant les programmes disponibles et commencez à préparer votre aventure internationale. Que vous souhaitiez partir pour un semestre, un stage, ou un projet de volontariat, MoveOn vous guide dans chaque étape de votre parcours.");
        p9.getStyle()
            .set("text-align", "justify")
            .set("width", "100%");
        
        Paragraph p10 = new Paragraph("Ne laissez pas passer cette opportunité unique d'enrichir votre expérience académique et de vivre une aventure internationale inoubliable. Votre voyage commence ici, sur MoveOn !");
        p10.getStyle()
            .set("text-align", "center")
            .set("width", "100%");
       
        // Ajouter tous les paragraphes à la page
        add(title, T2, p2, T3, p3, T4, p4, T5, p5, T6, p6, p7, p8, T7, p9, p10);

        // Appliquer un style global à la page
        this.getStyle().set("padding", "20px");
        this.getStyle().set("background-color", "#f4f4f4");
    }
}
