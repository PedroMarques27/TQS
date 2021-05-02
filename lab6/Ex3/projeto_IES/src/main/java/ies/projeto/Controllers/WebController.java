package ies.projeto.Controllers;

import ies.projeto.Entities.*;
import ies.projeto.Forms.*;
import ies.projeto.Forms.ImageForm;

import ies.projeto.Forms.LoginForm;
import ies.projeto.Forms.ProfileForm;
import ies.projeto.Forms.RegisterBusinessForm;
import ies.projeto.Forms.RegisterClientForm;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ies.projeto.Entities.Business;
import ies.projeto.Entities.ChatMessage;
import ies.projeto.Entities.Client;
import ies.projeto.Entities.Images;
import ies.projeto.Entities.Match;
import ies.projeto.Entities.Message;
import ies.projeto.Entities.Promotion;
import ies.projeto.Entities.User;
import ies.projeto.Repositories.BusinessRepository;
import ies.projeto.Repositories.ClientRepository;

import ies.projeto.Repositories.ImageRepository;

import ies.projeto.Repositories.MatchesRepository;
import ies.projeto.Repositories.MessageRepository;
import ies.projeto.Repositories.PromotionRepository;
import ies.projeto.Repositories.UserRepository;
import ies.projeto.Repositories.SubscriptionRepository;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.*;

@EnableScheduling
@EnableAsync
@Controller
public class WebController {

    @Autowired // This means to get the bean called userRepository
    private UserRepository userRepository;

    @Autowired // This means to get the bean called
    private MatchesRepository matchesRepository;


    @Autowired
    private BusinessRepository businessRepository;


    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;


    private HashMap<String, User> sessions = new HashMap<String, User>();
    private HashMap<User, Match> user_matches = new HashMap<User, Match>();
    private LinkedHashMap<ArrayList<String>, Integer> people_score = new LinkedHashMap<ArrayList<String>, Integer>(); //to calculate the score between two people

    // Login
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "LoginRegister/login";
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@ModelAttribute(name = "loginForm") LoginForm lf, HttpServletRequest request, Model model) {
        String email = lf.getEmail();
        String password = lf.getPassword();
        User currentUser = userRepository.existsUser(email.toLowerCase(), password);
        sessions.put(request.getSession().getId(), currentUser);
        if (currentUser != null) {
            return "redirect:home";
        }

        return "redirect:home";

    }

    // Register
    @RequestMapping(value = "/register_client", method = RequestMethod.GET)
    public String getRegisterForm(Model model) {
        model.addAttribute("registerForm", new RegisterClientForm());
        return "LoginRegister/register_client";
    }

    @RequestMapping(value = "/register_client", method = RequestMethod.POST)
        // Map ONLY POST Requests
    String addNewUser(@ModelAttribute(name = "registerForm") RegisterClientForm lf, HttpServletRequest request,
                      Model model) {

        String username = lf.getUsername();
        String email = lf.getEmail().toLowerCase();
        String password = lf.getPassword();
        String birthday = lf.getBirthday();
        String gender = lf.getGender();
        String sexual_orientation = lf.getSexual_orientation();
        String user_type = "Client";
        String state = "Accepted";
        Random rand = new Random();
        double lat = rand.nextDouble() * 180.0 - 90.0;
        double lon = rand.nextDouble() * 360.0 - 180.0;
        String local = lat + "," + lon;

        Date now = new Date();

        if (email.contains("@") && password.length() > 0) {
            User n = new User();
            Client p = new Client();
            p.setEmail(email);
            p.setBirthday(birthday);
            p.setGender(gender);
            p.setSexual_orientation(sexual_orientation);
            p.setUsername(username);
            p.setState(state);
            p.setProfile_picture("DEFAULT_F");
            p.setAge(0);
            String[] date = birthday.split("-");
            int year = Integer.parseInt(date[0]);
            int month = Integer.parseInt(date[1]);
            int day = Integer.parseInt(date[2]);
            LocalDate today = LocalDate.now();                          //Today's date
            LocalDate birthdayDate = LocalDate.of(year, month, day);  //Birth date

            Period period = Period.between(birthdayDate, today);

            p.setAge(period.getYears());

            n.setPassword(password);
            n.setEmail(email);
            n.setTipo(user_type);
            n.setData_de_adesao(now);
            n.setLocal(local);
            userRepository.save(n);
            clientRepository.save(p);
            User currentUser = userRepository.existsUser(email.toLowerCase(), password);
            sessions.put(request.getSession().getId(), currentUser);

            genScores(currentUser);
            return "redirect:home";
        }
        return "redirect:register_client";

    }

    // Register
    @RequestMapping(value = "/register_business", method = RequestMethod.GET)
    public String getRegisterFormBusiness(Model model) {
        model.addAttribute("registerForm", new RegisterBusinessForm());
        return "LoginRegister/register_business";
    }

    @RequestMapping(value = "/register_business", method = RequestMethod.POST)
        // Map ONLY POST Requests
    String addNewBusiness(@ModelAttribute(name = "registerForm") RegisterBusinessForm lf, HttpServletRequest request,
                          Model model) {
        Random rand = new Random();

        String email = lf.getEmail();
        String password = lf.getPassword();
        String name = lf.getName();
        String address = lf.getAddress();
        String business_type = lf.getBusinessType();

        double lat = rand.nextDouble() * 180.0 - 90.0;
        double lon = rand.nextDouble() * 360.0 - 180.0;
        String local = lat + "," + lon;

        String user_type = "Business";
        String business_state = "Pending";
        Date now = new Date();
        if (email.contains("@") && password.length() > 0) {
            User n = new User();
            Business b = new Business();

            b.setEmail(email);
            b.setName(name);
            b.setBusiness_type(business_type);
            b.setAddress(address);
            b.setBusiness_state(business_state);

            n.setPassword(password);
            n.setEmail(email);
            n.setTipo(user_type);
            n.setData_de_adesao(now);
            n.setLocal(local);
            userRepository.save(n);
            businessRepository.save(b);
            User currentUser = userRepository.existsUser(email.toLowerCase(), password);
            sessions.put(request.getSession().getId(), currentUser);
            return "redirect:home";
        }
        return "redirect:register_business";

    }

    // Profile
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String getProfile(Model model, HttpServletRequest request) {
        User currentUser = sessions.get(request.getSession().getId());
        if (currentUser == null) {
            return "redirect:login";
        }

        Client currentClient = clientRepository.getByEmail(currentUser.getEmail());
        model.addAttribute("user", currentClient);

        ArrayList<Match> talkingMatches = matchesRepository.findDecidedMatchByEmailTalked(currentClient.getEmail());
        ArrayList<String> allTalkingClientEmails = getMatchesEmails(talkingMatches, currentUser.getEmail());
        ArrayList<Client> allTalkingClients = getClientsByEmails(allTalkingClientEmails);
        // all_matched_clients.addAll(currentDecidedMatchedClientsNotTalking);
        HashMap<String, String> client_image = new HashMap<String, String>();
        for (Client c : allTalkingClients) {
            User client_user = userRepository.getByEmail(c.getEmail());
            String s = null;
            if (c.getProfile_picture().contains("DEFAULT_")) {
                s = c.getProfile_picture() + ".png";
            } else {
                Images currentImageObject = imageRepository.findByNameAndUser(c.getProfile_picture(), client_user.getId());

                try {
                    if (currentImageObject != null) {
                        byte[] encodeBase64 = Base64.getEncoder().encode(currentImageObject.getData());
                        s = new String(encodeBase64, "UTF-8");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
            if (s == null) {
                s = "DEFAULT_O.png";
            }
            client_image.put(c.getEmail(), s);
        }

        String s = null;
        if (currentClient.getProfile_picture().contains("DEFAULT_")) {
            s = currentClient.getProfile_picture() + ".png";
        } else {
            Images currentImageObject = imageRepository.findByNameAndUser(currentClient.getProfile_picture(), currentUser.getId());

            try {
                if (currentImageObject != null) {
                    byte[] encodeBase64 = Base64.getEncoder().encode(currentImageObject.getData());
                    s = new String(encodeBase64, "UTF-8");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        if (s == null) {
            s = "DEFAULT_O.png";
        }

        model.addAttribute("profile_pic", s);
        model.addAttribute("images_map", client_image);
        model.addAttribute("all_clients", allTalkingClients);


        return "Client/profile";
    }

    @RequestMapping(value = "/client_profile", method = RequestMethod.GET)
    public String getClientProfile(@RequestParam(name = "email", required = true) String email, Model model, HttpServletRequest request) {
        User currentUser = sessions.get(request.getSession().getId());
        if (currentUser == null) {
            return "redirect:login";
        }

        Client client = clientRepository.getByEmail(email);
        model.addAttribute("user", client);

        String s = null;
        if (client.getProfile_picture().contains("DEFAULT_")) {
            s = client.getProfile_picture() + ".png";
        } else {
            Images currentImageObject = imageRepository.findByNameAndUser(client.getProfile_picture(), currentUser.getId());

            try {
                if (currentImageObject != null) {
                    byte[] encodeBase64 = Base64.getEncoder().encode(currentImageObject.getData());
                    s = new String(encodeBase64, "UTF-8");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        if (s == null) {
            s = "DEFAULT_O.png";
        }

        model.addAttribute("profile_pic", s);

        return "Client/other_client_profile";
    }

    @Autowired
    private ImageRepository imageRepository;

    // Register
    @RequestMapping(value = "/edit_profile", method = RequestMethod.GET)
    public String getProfileEdit(Model model, HttpServletRequest request) {
        User currentUser = sessions.get(request.getSession().getId());
        if (currentUser == null) {
            return "redirect:login";
        }
        Client currentClient = clientRepository.getByEmail(currentUser.getEmail());

        List<Images> clientUploadedImages = imageRepository.findByUserID(currentUser.getId());
        String currentImage = "DEFAULT_O";
        HashMap<String, String> imgs_src = new HashMap<>();
        for (Images i : clientUploadedImages) {
            byte[] encodeBase64 = Base64.getEncoder().encode(i.getData());
            String s;
            System.out.println(i.getFileName());
            try {
                s = new String(encodeBase64, "UTF-8");
                imgs_src.put(i.getFileName(), s);
                if (i.getFileName().equals(currentClient.getProfile_picture())) {
                    currentImage = s;
                }
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        if (currentClient.getProfile_picture().contains("DEFAULT_")) {
            currentImage = currentClient.getProfile_picture() + ".png";
        }


        model.addAttribute("profile_pic", currentImage);
        model.addAttribute("images", imgs_src);
        model.addAttribute("user", currentClient);
        model.addAttribute("editProfileForm", new ProfileForm());
        model.addAttribute("imageForm", new ImageForm());
        model.addAttribute("userID", currentUser.getId());
        return "Client/edit_profile";
    }

    @RequestMapping(value = "/edit_profile", method = RequestMethod.POST)
        // Map ONLY POST Requests
    String editProfile(@ModelAttribute(name = "editProfileForm") ProfileForm lf, Model model,
                       HttpServletRequest request) {
        User currentUser = sessions.get(request.getSession().getId());
        Client currentClient = clientRepository.getByEmail(sessions.get(request.getSession().getId()).getEmail());
        String description = lf.getDescription();
        String bio = lf.getBio();
        String interests = lf.getInterests();
        String profile_image = lf.getProfile_picture();
        if (profile_image != null) {
            currentClient.setProfile_picture(profile_image);
        }
        int age = lf.getAge();
        if (age > 0 && age < 150) {
            currentClient.setAge(age);
        }
        if (bio.length() > 5 && bio.length() < 500)
            currentClient.setBio(bio);
        if (interests.length() > 5 && interests.length() < 500)
            currentClient.setInterests(interests);
        if (description.length() > 5 && description.length() < 50)
            currentClient.setDescription(description);
        clientRepository.save(currentClient);
        return "redirect:profile";
    }

    @RequestMapping(value = "/match", method = RequestMethod.GET)
    public String listStudent(Model model, HttpServletRequest request) {
        User currentUser = sessions.get(request.getSession().getId());

        if (currentUser == null)
            return "redirect:login";
        genScores(currentUser);
        ArrayList<Match> undecidedMatches = matchesRepository.findUndecidedMatchByEmail(currentUser.getEmail());
        if (undecidedMatches.size() == 0)
            return "redirect:home";

        ArrayList<String> allUndecidedClientEmails = getMatchesEmails(undecidedMatches, currentUser.getEmail());
        ArrayList<Client> undecidedClient = getClientsByEmails(allUndecidedClientEmails);

        Client currentClient = clientRepository.getByEmail(currentUser.getEmail());

        Client g = undecidedClient.get(0);
        User matched_user = userRepository.getByEmail(g.getEmail());
        Match currentMatch = matchesRepository.findMatchByEmails(currentClient.getEmail(), g.getEmail());
        user_matches.put(currentUser, currentMatch);
        undecidedClient.remove(g);


        List<Images> clientUploadedImages = imageRepository.findByUserID(matched_user.getId());
        List<String> images = new ArrayList<>();

        for (Images i : clientUploadedImages) {
            byte[] encodeBase64 = Base64.getEncoder().encode(i.getData());
            String s;
            System.out.println(i.getFileName());
            try {
                s = new String(encodeBase64, "UTF-8");
                images.add(s);

            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        if (images.size() == 0) {
            images.add(g.getProfile_picture() + ".png");
        }


        model.addAttribute("images", images);
        model.addAttribute("matched_client", g);
        return "Client/matches.html";
    }


    @GetMapping(path = "/home")
    public String getHomePage(Model model, HttpServletRequest request, HttpSession session) {
        User currentUser = sessions.get(request.getSession().getId());
        if (currentUser == null) {
            return "redirect:login";
        }
        if (currentUser.getTipo().equals("Admin")) {
            ArrayList<User> businesses = userRepository.getByTipo("Business");
            ArrayList<User> clients = userRepository.getByTipo("Client");

            ArrayList<Business> busRep = new ArrayList<>();
            ArrayList<Business> busPend = new ArrayList<>();
            ArrayList<Business> busAcc = new ArrayList<>();
            ArrayList<Client> cliPend = new ArrayList<>();
            ArrayList<Client> cliRep = new ArrayList<>();
            ArrayList<Client> cliAcc = new ArrayList<>();

            for (User user : businesses) {
                Business b = businessRepository.getByEmail(user.getEmail());
                if (b != null) {
                    if (b.getBusiness_state().equals("Pending")) {
                        busPend.add(b);
                    } else if (b.getBusiness_state().equals("Reported")) {
                        busRep.add(b);
                    } else if (b.getBusiness_state().equals("Accepted"))
                        busAcc.add(b);
                }

            }
            for (User user : clients) {
                Client c = clientRepository.getByEmail(user.getEmail());
                if (c != null) {
                    if (c.getState().equals("Pending")) {
                        cliPend.add(c);
                    } else if (c.getState().equals("Reported")) {
                        cliRep.add(c);
                    } else if (c.getState().equals("Accepted"))
                        cliAcc.add(c);
                }

            }
            model.addAttribute("businessesRep", busRep);
            model.addAttribute("businessesPend", busPend);
            model.addAttribute("businessesAcc", busAcc);
            model.addAttribute("clientsRep", cliRep);
            model.addAttribute("clientsPend", cliPend);
            model.addAttribute("clientsAcc", cliAcc);

            return "Admin/dashboard";
        }
        session.setAttribute("userID", currentUser.getId());
        if (currentUser != null) {
            if (currentUser.getTipo().equals("Client")) {
                ArrayList<Match> decidedMatchesNotTalking = matchesRepository.findDecidedMatchByEmailNotTalked(currentUser.getEmail());
                ArrayList<String> allDecidedClientEmails = getMatchesEmails(decidedMatchesNotTalking, currentUser.getEmail());
                ArrayList<Client> undecidedClients = getClientsByEmails(allDecidedClientEmails);
                HashMap<String, String> client_image = new HashMap<String, String>();
                for (Client c : undecidedClients) {
                    User client_user = userRepository.getByEmail(c.getEmail());
                    String s = null;
                    if (c.getProfile_picture().contains("DEFAULT_")) {
                        s = c.getProfile_picture() + ".png";
                    } else {
                        Images currentImageObject = imageRepository.findByNameAndUser(c.getProfile_picture(), client_user.getId());

                        try {
                            if (currentImageObject != null) {
                                byte[] encodeBase64 = Base64.getEncoder().encode(currentImageObject.getData());
                                s = new String(encodeBase64, "UTF-8");
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }
                    if (s == null) {
                        s = "DEFAULT_O.png";
                    }
                    client_image.put(c.getEmail(), s);
                }


                List<Promotion> promotions = promotionRepository.findAll();
                List<HashMap<Promotion, String>> list_of_promotions = new ArrayList<HashMap<Promotion, String>>();  
                int max = 3;
                if (promotions.size()<3)
                    max=1;  
                if (promotions.size()!=0){
                for (int i=0;i<max; i++) {

                    HashMap<Promotion, String> promotion_image = new HashMap<Promotion, String>();
                    int index = (int) (Math.random() * (promotions.size()));
                    Promotion promotion = promotions.get(index);

                    User user = userRepository.getByEmail(promotion.getEmail());
                    String s = null;
                    if (promotion.getImage_name().contains("DEFAULT_")) {
                        s = promotion.getImage_name() + ".jpg";
                    } else {
                        Images currentImageObject = imageRepository.findByNameAndUser(promotion.getImage_name(), user.getId());
                        try {
                            if (currentImageObject != null) {
                                byte[] encodeBase64 = Base64.getEncoder().encode(currentImageObject.getData());
                                s = new String(encodeBase64, "UTF-8");
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }
                    if (s == null) {
                        s = "DEFAULT_O.png";
                    }
                    promotion_image.put(promotion, s);
                    list_of_promotions.add(promotion_image);
                }
            }

                
               
                if (list_of_promotions.size()!=0){

                    model.addAttribute("first", list_of_promotions.get(0).keySet().toArray()[0]);
                    model.addAttribute("first_s", list_of_promotions.get(0).values().toArray()[0]);
                    if (list_of_promotions.size()>1){
                        model.addAttribute("second", list_of_promotions.get(1).keySet().toArray()[0]);
                        model.addAttribute("second_s", list_of_promotions.get(1).values().toArray()[0]);
                    }
                    if (list_of_promotions.size()>2){
                        model.addAttribute("third", list_of_promotions.get(2).keySet().toArray()[0]);
                        model.addAttribute("third_s", list_of_promotions.get(2).values().toArray()[0]);
                    }
                    

                    
                    model.addAttribute("promotion_images", list_of_promotions);
                }
                
                model.addAttribute("images_map",client_image);
                model.addAttribute("clients", undecidedClients);
                return "Client/homepage";
            } else if (currentUser.getTipo().equals("Business")) {
                Business currentBusiness = businessRepository.getByEmail(currentUser.getEmail());
                ArrayList<Promotion> promotions = promotionRepository.getByEmail(currentBusiness.getEmail());

                HashMap<Promotion, String> data = new HashMap<Promotion, String>();


                for (Promotion c : promotions) {

                    String s = null;
                    if (c.getImage_name().contains("DEFAULT_")) {
                        s = c.getImage_name() + ".jpg";
                    } else {
                        Images currentImageObject = imageRepository.findByNameAndUser(c.getImage_name(), currentUser.getId());

                        try {
                            if (currentImageObject != null) {
                                byte[] encodeBase64 = Base64.getEncoder().encode(currentImageObject.getData());
                                s = new String(encodeBase64, "UTF-8");
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }
                    if (s == null) {
                        s = "DEFAULT_O.png";
                    }
                    data.put(c, s);
                }
                model.addAttribute("promotions", data);
                return "Business/homepage_business";
            }
        }
        return "redirect:login";
    }


    @GetMapping(path = "/change_state_business")
    public String getChangeStateBusiness(@RequestParam(name = "email", required = true) String email, Model model, HttpServletRequest request) {
        User currentUser = sessions.get(request.getSession().getId());
        if (currentUser == null) {
            return "redirect:login";
        }
        Business business = businessRepository.getByEmail(email);
        model.addAttribute("business", business);
        model.addAttribute("businessStateForm", new BusinessStateForm());
        return "Admin/change_state_business";
    }

    @PostMapping(path = "/change_state_business")
    String changeStateBusiness(@ModelAttribute(name = "businessStateForm") BusinessStateForm bf, @RequestParam(name = "email", required = true) String email, Model model, HttpServletRequest request) {
        User currentUser = sessions.get(request.getSession().getId());
        if (currentUser == null) {
            return "redirect:login";
        }
        Business business = businessRepository.getByEmail(email);
        String state = bf.getBusiness_state();

        if (state.length() > 0)
            business.setBusiness_state(state);
        System.out.println("------------------------------>");
        businessRepository.save(business);
        return "redirect:home";
    }

    @GetMapping(path = "/change_state_client")
    public String getChangeStateClient(@RequestParam(name = "email", required = true) String email, Model model, HttpServletRequest request) {
        User currentUser = sessions.get(request.getSession().getId());
        if (currentUser == null) {
            return "redirect:login";
        }
        Client client = clientRepository.getByEmail(email);
        model.addAttribute("client", client);
        model.addAttribute("clientStateForm", new ClientStateForm());
        return "Admin/change_state_client";
    }

    @PostMapping(path = "/change_state_client")
    String changeStateClient(@ModelAttribute(name = "clientStateForm") ClientStateForm cf, String email, Model model, HttpServletRequest request) {
        User currentUser = sessions.get(request.getSession().getId());
        if (currentUser == null) {
            return "redirect:login";
        }
        Client client = clientRepository.getByEmail(email);
        String state = cf.getState();

        if (state != null)
            client.setState(state);

        clientRepository.save(client);
        return "redirect:home";
    }

    @GetMapping(path = "/delete_user")
    public String getDeleteUser(@RequestParam(name = "email", required = true) String email, Model model,
                                HttpServletRequest request) {
        User currentUser = sessions.get(request.getSession().getId());
        if (currentUser == null) {
            return "redirect:login";
        }
        User user = userRepository.getByEmail(email);
        if (user.getTipo().equals("Client")) {
            clientRepository.deleteByEmail(email);
        } else if (user.getTipo().equals("Business")) {
            Business currentBusiness = businessRepository.getByEmail(email);
            ArrayList<Promotion> promotions = promotionRepository.getByEmail(currentBusiness.getEmail());
            ArrayList<Subscription> subscriptions = subscriptionRepository.getByEmail(currentBusiness.getEmail());

            for (Promotion p : promotions)
                promotionRepository.deletePromotionById(p.getId());
            for (Subscription s : subscriptions)
                subscriptionRepository.deleteSubscriptionById(s.getId());
            System.out.println("hello");
            System.out.println("");
            businessRepository.deleteByEmail(email);
        }
        userRepository.deleteUsersById(user.getId());
        if (currentUser.getTipo().equals("Admin"))
            return "redirect:home";
        return "redirect:login";
    }

    @GetMapping(path = "/report_user")
    public String reportUser(@RequestParam(name = "email", required = true) String email, Model model,
                             HttpServletRequest request) {
        User currentUser = sessions.get(request.getSession().getId());
        if (currentUser == null) {
            return "redirect:login";
        }
        User user = userRepository.getByEmail(email);
        if (user.getTipo().equals("Client")) {
            Client client = clientRepository.getByEmail(email);
            client.setState("Reported");
            clientRepository.save(client);
        } else if (user.getTipo().equals("Business")) {
            Business business = businessRepository.getByEmail(email);
            business.setBusiness_state("Reported");
            businessRepository.save(business);
        }
        return "redirect:home";
    }

    @GetMapping(path = "/swipe_right")
    public String SwipeRight(Model model, HttpServletRequest request) {

        User currentUser = sessions.get(request.getSession().getId());
        Client currentClient = clientRepository.getByEmail(currentUser.getEmail());
        Match currentMatch = user_matches.get(currentUser);

        if (currentMatch.getEmail1().equals(currentClient.getEmail())) {
            currentMatch.setSwipe_email1(true);
        } else {
            currentMatch.setSwipe_email2(true);
        }
        matchesRepository.save(currentMatch);
        return "redirect:match";
    }

    @GetMapping(path = "/swipe_left")
    public String SwipeLeft(Model model, HttpServletRequest request) {
        User currentUser = sessions.get(request.getSession().getId());
        Client currentClient = clientRepository.getByEmail(currentUser.getEmail());
        Match currentMatch = user_matches.get(currentUser);

        if (currentMatch.getEmail1().equals(currentClient.getEmail())) {
            currentMatch.setSwipe_email1(false);
        } else {
            currentMatch.setSwipe_email2(false);
        }
        matchesRepository.deleteById(currentMatch.getId());
        return "redirect:match";
    }

    public ArrayList<String> getMatchesEmails(ArrayList<Match> matches, String email) {
        ArrayList<String> emails = new ArrayList<>();
        for (Match m : matches) {

            if (m.getEmail1().equals(email))
                emails.add(m.getEmail2());
            else
                emails.add(m.getEmail1());

        }
        return emails;
    }


    public ArrayList<Client> getClientsByEmails(List<String> emails) {
        ArrayList<Client> clients = new ArrayList<>();
        for (String email : emails) {
            clients.add(clientRepository.getByEmail(email));
        }
        return clients;
    }

    @GetMapping(path = "/all_users")
    public @ResponseBody
    Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the users

        return userRepository.findAll();
    }

    @GetMapping(path = "/all_business")
    public @ResponseBody
    Iterable<Business> getAllBusiness() {
        // This returns a JSON or XML with the users

        return businessRepository.findAll();
    }

    @GetMapping(path = "/all_matches")
    public @ResponseBody
    Iterable<Match> getAllMatches() {
        // This returns a JSON or XML with the users
        return matchesRepository.findAll();
    }

    @GetMapping(path = "/info")
    public String getInfo(Model model, HttpServletRequest request) {
        User currentUser = sessions.get(request.getSession().getId());
        if (currentUser == null) {
            return "redirect:login";
        }
        Business currentBusiness = businessRepository.getByEmail(sessions.get(request.getSession().getId()).getEmail());
        model.addAttribute("user", currentBusiness);

        ArrayList<Subscription> oldSubs = new ArrayList<>();
        Subscription currentSubscription = new Subscription();
        ArrayList<Subscription> subs = subscriptionRepository.getByEmail(currentBusiness.getEmail());

        for (Subscription sub : subs) {
            if (sub.getEnd_date() == null) {
                currentSubscription = sub;
            } else
                oldSubs.add(sub);
        }
        System.out.println(oldSubs);

        model.addAttribute("currentSub", currentSubscription);
        model.addAttribute("oldSubscriptions", oldSubs);

        return "Business/info";
    }

    @RequestMapping(value = "/edit_business_info", method = RequestMethod.GET)
    public String getBusinessEdit(Model model, HttpServletRequest request) {
        User currentUser = sessions.get(request.getSession().getId());
        if (currentUser == null) {
            return "redirect:login";
        }
        Business currentBusiness = businessRepository.getByEmail(sessions.get(request.getSession().getId()).getEmail());
        model.addAttribute("user", currentBusiness);
        model.addAttribute("editBusinessForm", new BusinessForm());
        return "Business/edit_business_info";
    }

    @RequestMapping("/chat")
    public String chat(@RequestParam(name = "message_to", required = false) String message_to, Model model, HttpServletRequest request) {
        User currentUser = sessions.get(request.getSession().getId());
        if (currentUser == null) {
            return "redirect:login";
        }
        Match m = matchesRepository.findMatchByEmails(currentUser.getEmail(), message_to);
        m.setTalking(true);
        matchesRepository.save(m);

        List<String> topic = new ArrayList<>();
        topic.add(message_to);
        topic.add(currentUser.getEmail());
        Collections.sort(topic);
        List<Message> messages = messageRepository.getUsersPreviousMessages(topic.toString());
        if (messages == null)
            messages = new ArrayList<>();

        System.out.println(topic);
        model.addAttribute("previous_message", messages.toArray());
        model.addAttribute("sender", currentUser.getEmail());
        model.addAttribute("receiver", message_to);
        return "Client/chat";
    }


    @RequestMapping(value = "/home", method = RequestMethod.POST)
    public String goToChat(@RequestParam(name = "message_to", required = false) String email, Model model) {

        return "redirect:chat?message_to=" + email;
    }

    @RequestMapping(value = "/edit_business_info", method = RequestMethod.POST)
        // Map ONLY POST Requests
    String editBusinessInfo(@ModelAttribute(name = "editBusinessForm") BusinessForm bf, Model model, HttpServletRequest request) {
        User currentUser = sessions.get(request.getSession().getId());
        if (currentUser == null) {
            return "redirect:login";
        }
        Business currentBusiness = businessRepository.getByEmail(sessions.get(request.getSession().getId()).getEmail());
        String name = bf.getName();
        String address = bf.getAddress();
        String description = bf.getDescription();
        String business_type = bf.getBusiness_type();

        if (name.length() > 0 && name.length() < 500)
            currentBusiness.setName(name);
        if (address.length() > 5 && address.length() < 200)
            currentBusiness.setAddress(address);
        if (description.length() > 5 && description.length() < 50)
            currentBusiness.setDescription(description);
        if (business_type.length() > 1 && business_type.length() < 100)
            currentBusiness.setBusiness_type(business_type);

        businessRepository.save(currentBusiness);
        return "redirect:info";
    }

    @GetMapping(path = "/change_subscription")
    public String getChangeSubscription(@RequestParam(name = "id", required = true) int id, Model model, HttpServletRequest request) {
        User currentUser = sessions.get(request.getSession().getId());
        if (currentUser == null) {
            return "redirect:login";
        }
        Subscription subscription = subscriptionRepository.getById(id);
        model.addAttribute("subscription", subscription);
        model.addAttribute("subscriptionForm", new SubscriptionForm());
        return "Business/change_subscription";
    }

    @PostMapping(path = "/change_subscription")
    String changeSubscription(@ModelAttribute(name = "subscriptionForm") SubscriptionForm sf, int id, Model model, HttpServletRequest request) {
        User currentUser = sessions.get(request.getSession().getId());
        if (currentUser == null) {
            return "redirect:login";
        }
        Subscription subscription = subscriptionRepository.getById(id);
        int old_monthly_fee = subscription.getMonthly_fee();
        int monthly_fee = sf.getMonthly_fee();

        if (monthly_fee != old_monthly_fee) {
            Date now = new Date();
            subscription.setEnd_date(now);

            Subscription newSub = new Subscription();
            newSub.setEmail(currentUser.getEmail());
            newSub.setStart_date(now);
            newSub.setEnd_date(null);
            newSub.setMonthly_fee(monthly_fee);
            subscriptionRepository.save(newSub);
        }
        return "redirect:info";
    }

    @GetMapping(path = "/promotion_details")
    public String getPromotionDetails(@RequestParam(name = "id", required = true) int id, Model model, HttpServletRequest request) {
        User currentUser = sessions.get(request.getSession().getId());
        Promotion currentPromotion = promotionRepository.getById(id);

        if (currentUser == null)
            return "redirect:login";

        if (!currentUser.getTipo().equals("Client")) {
            Business currentBusiness = businessRepository.getByEmail(currentUser.getEmail());

            if (currentPromotion.getEmail().equalsIgnoreCase(currentUser.getEmail())) {
                model.addAttribute("isOwner", true);
            } else {
                model.addAttribute("isOwner", false);
                currentPromotion.setClicks(currentPromotion.getClicks() + 1);
                promotionRepository.save(currentPromotion);
            }


        } else {
            currentPromotion.setClicks(currentPromotion.getClicks() + 1);
            model.addAttribute("isOwner", false);
            promotionRepository.save(currentPromotion);
        }

        System.out.println(currentPromotion.getTitle()+"-------------------------------"+ currentPromotion.getEmail());

        Business currentBusiness = businessRepository.getByEmail(currentPromotion.getEmail());
        String s = "DEFAULT_R.jpg";
        if (currentPromotion.getImage_name().contains("DEFAULT_")) {
            s = currentPromotion.getImage_name() + ".jpg";
        } else {
            Images currentImageObject = imageRepository.findByNameAndUser(currentPromotion.getImage_name(), currentUser.getId());
            try {
                if (currentImageObject != null) {
                    byte[] encodeBase64 = Base64.getEncoder().encode(currentImageObject.getData());
                    s = new String(encodeBase64, "UTF-8");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        model.addAttribute("image", s);
        System.out.println(currentBusiness.getEmail());
        model.addAttribute("business", currentBusiness);
        model.addAttribute("promotion", currentPromotion);
        return "Business/promotion";


    }

    @GetMapping(path = "/new_promotion")
    public String getNewPromotion(Model model, HttpServletRequest request) {
        User currentUser = sessions.get(request.getSession().getId());
        if (currentUser == null) {
            return "redirect:login";
        }

        model.addAttribute("newPromotionForm", new PromotionForm());
        return "Business/new_promotion";
    }

    @PostMapping(path = "/new_promotion")
    String newPromotion(@ModelAttribute(name = "editPromotionForm") PromotionForm pf, Model model, HttpServletRequest request) {
        User currentUser = sessions.get(request.getSession().getId());
        if (currentUser == null) {
            return "redirect:login";
        }
        String title = pf.getTitle();
        String promotion_code = pf.getPromotion_code();
        String description = pf.getDescription();
        // String image =

        if (title.length() > 0 && promotion_code.length() > 0 && description.length() > 0) {
            Promotion p = new Promotion();
            p.setTitle(title);
            p.setPromotion_code(promotion_code);
            p.setDescription(description);
            p.setEmail(currentUser.getEmail());
            p.setImage_name("DEFAULT_R");
            promotionRepository.save(p);
        }
        return "redirect:home";
    }

    @GetMapping(path = "/edit_promotion")
    public String getPromotionEdit(@RequestParam(name = "id", required = true) int id, Model model, HttpServletRequest request) {
        User currentUser = sessions.get(request.getSession().getId());
        if (currentUser == null) {
            return "redirect:login";
        }
        Promotion promotion = promotionRepository.getById(id);


        List<Images> clientUploadedImages = imageRepository.findByUserID(currentUser.getId());
        String currentImage = "DEFAULT_R";
        HashMap<String, String> imgs_src = new HashMap<>();
        for (Images i : clientUploadedImages) {
            byte[] encodeBase64 = Base64.getEncoder().encode(i.getData());
            String s;
            System.out.println(i.getFileName());
            try {
                s = new String(encodeBase64, "UTF-8");
                imgs_src.put(i.getFileName(), s);
                if (i.getFileName().equals(promotion.getImage_name())) {
                    currentImage = s;
                }
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        if (promotion.getImage_name().contains("DEFAULT_")) {
            currentImage = promotion.getImage_name() + ".jpg";
        }


        model.addAttribute("promotion_pic", currentImage);
        model.addAttribute("images", imgs_src);
        model.addAttribute("imageForm", new ImageForm());
        model.addAttribute("userID", currentUser.getId());
        model.addAttribute("promotion", promotion);
        model.addAttribute("editPromotionForm", new PromotionForm());

        return "Business/edit_promotion";


    }

    @PostMapping(path = "/edit_promotion")
    String editPromotion(@ModelAttribute(name = "editPromotionForm") PromotionForm pf, int id, Model model, HttpServletRequest request) {
        User currentUser = sessions.get(request.getSession().getId());
        if (currentUser == null) {
            return "redirect:login";
        }

        Promotion promotion = promotionRepository.getById(id);
        String title = pf.getTitle();
        String promotion_code = pf.getPromotion_code();
        String description = pf.getDescription();
        String image = pf.getPromotion_picture();
        if (title.length() > 0 && title.length() < 500)
            promotion.setTitle(title);
        if (promotion_code.length() > 5 && promotion_code.length() < 200)
            promotion.setPromotion_code(promotion_code);
        if (description.length() > 5 && description.length() < 50)
            promotion.setDescription(description);
        if (image != null) {
            promotion.setImage_name(image);
        }
        System.out.println(promotion.getTitle());
        System.out.println(promotion.getImage_name());
        System.out.println(promotion.getPromotion_code());
        promotionRepository.save(promotion);
        return "redirect:home";
    }

  
    @GetMapping("/delete_account")
    public String deleteAccount(Model model, HttpServletRequest request, HttpSession session){
        User currentUser = sessions.get(request.getSession().getId());
        userRepository.delete(currentUser);
        if (currentUser.getTipo().equals("Client"))
            clientRepository.deleteByEmail(currentUser.getEmail());
        else
            businessRepository.deleteByEmail(currentUser.getEmail());

        matchesRepository.deleteByEmail(currentUser.getEmail());
        messageRepository.deleteByEmail(currentUser.getEmail());
		imageRepository.deleteByUserID(currentUser.getId());
        subscriptionRepository.deleteByEmail(currentUser.getEmail());
        promotionRepository.deleteByEmail(currentUser.getEmail());
        sessions.remove(request.getSession().getId());
        return "redirect:login";
    }
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessagePojo) {

        List<String> emails = new ArrayList<>();
        emails.add(chatMessagePojo.getSender());
        emails.add(chatMessagePojo.getReceiver());
        Collections.sort(emails);
        Message m = new Message();
        m.setConversation(emails.toString());
        m.setReceiver(chatMessagePojo.getReceiver());
        m.setSender(chatMessagePojo.getSender());
        m.setContent(chatMessagePojo.getContent());
        messageRepository.save(m);
        return chatMessagePojo;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/messages")
    public ChatMessage addUser(@Payload ChatMessage chatMessagePojo, SimpMessageHeaderAccessor headerAccessor) {

// Add username in web socket session
        headerAccessor.getSessionAttributes().put("sender", chatMessagePojo.getSender());
        headerAccessor.getSessionAttributes().put("receiver", chatMessagePojo.getReceiver());
        return chatMessagePojo;
    }

    @Async
    @Scheduled(fixedRate = 10000)
    public void genLocation() {
        if (!sessions.isEmpty()) {
            for (User currentUser : sessions.values()) {
                try {
                    //Get last location
                    System.out.println(currentUser.getEmail() + "  -- " + currentUser.getLocal());
                    String local = currentUser.getLocal();
                    //Pass location to the API
                    URL url = new URL("http://localhost:3000/gen-location/" + local);
                    System.out.println(local);
                    //Make GET Request
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    //Get response code
                    int responsecode = conn.getResponseCode();

                    if (responsecode != 200) {
                        throw new RuntimeException("HttpResponseCode: " + responsecode);
                    } else {

                        String inline = "";
                        Scanner scanner = new Scanner(url.openStream());

                        //Write all the JSON data into a string
                        while (scanner.hasNext()) {
                            inline += scanner.nextLine();
                        }
                        scanner.close();

                        //Parse the string into a json object
                        JSONParser parse = new JSONParser();
                        JSONObject data_obj = (JSONObject) parse.parse(inline);

                        //Get the required object from the above created object
                        JSONObject obj = (JSONObject) data_obj.get("location");

                        //Get the required data using its key
                        double lat = (double) obj.get("latitude");
                        double lon = (double) obj.get("longitude");

                        local = lat + "," + lon;
                        currentUser.setLocal(local);
                        userRepository.save(currentUser);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void fillMatches(User currentUser) {
        for (ArrayList<String> al : people_score.keySet()) {
            if (al.get(0).equals(currentUser.getEmail())) {
                if (matchesRepository.findMatchByEmails(currentUser.getEmail(), al.get(1)) == null && !currentUser.getEmail().equals(al.get(1))) {

                    Match m = new Match();
                    m.setEmail1(al.get(0));
                    m.setEmail2(al.get(1));
                    m.setSwipe_email1(false);
                    m.setSwipe_email2(false);
                    m.setTalking(false);
                    matchesRepository.save(m);

                }
            }
        }
    }

    public void genScores(User currentUser) {
        int counter = 0;
        int limit = 1;
        if (userRepository.findAll().size() < limit) {
            limit = userRepository.findAll().size();
        }

        if (currentUser != null && currentUser.getTipo().equals("Client")) {
            Client currentClient = clientRepository.getByEmail(currentUser.getEmail());
            String orientation = currentClient.getSexual_orientation();
            List<Client> filtered_users = clientRepository.getBySexualOrientation("O");
            if (orientation.equals("M") || orientation.equals("W")) {
                filtered_users.addAll(clientRepository.getBySexualOrientation(orientation));
            } else {
                filtered_users = clientRepository.findAll();
            }


            for (Client otherClient : filtered_users) {
                User otherUser = userRepository.getByEmail(otherClient.getEmail());
                if (counter == limit) {
                    return;
                }
                if (matchesRepository.findMatchByEmails(currentUser.getEmail(), otherUser.getEmail()) != null) {
                    continue;
                }
                if (currentUser.getEmail().equals(otherUser.getEmail())) {
                    continue;
                }
                if (currentUser != otherUser) {
                    int score = 1;
                    ArrayList<String> names = new ArrayList<>();
                    names.add(currentUser.getEmail());
                    names.add(otherUser.getEmail());

                    try {
                        String[] currClientInterests = currentClient.getInterests().split(",");
                        String[] otherClientInterests = otherClient.getInterests().split(",");

                        for (String i1 : currClientInterests) {
                            for (String i2 : otherClientInterests) {
                                if (i1.equals(i2))
                                    score++;
                            }
                        }
                    } catch (Exception ex) {
                        score = -3;
                    }

                    String[] currLoc = currentUser.getLocal().split(",");
                    String[] otherLoc = currentUser.getLocal().split(",");

                    double diffMeters = measure(Double.parseDouble(currLoc[0]), Double.parseDouble(currLoc[1]), Double.parseDouble(otherLoc[0]), Double.parseDouble(currLoc[0]));
                    if (diffMeters <= 3000)
                        score++;
                    if (diffMeters <= 9000)
                        score++;
                    if (diffMeters <= 15000)
                        score++;
                    if (score >= 3) {
                        counter++;
                        people_score.put(names, score);
                        people_score = sortByValue(people_score);
                        fillMatches(currentUser);
                    }
                }
            }

        }
    }


    private double measure(double lat1, double lon1, double lat2, double lon2) {  // generally used geo measurement function
        double R = 6378.137; // Radius of earth in KM
        double dLat = lat2 * Math.PI / 180 - lat1 * Math.PI / 180;
        double dLon = lon2 * Math.PI / 180 - lon1 * Math.PI / 180;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        return d * 1000; // meters
    }

    public static LinkedHashMap<ArrayList<String>, Integer> sortByValue(LinkedHashMap<ArrayList<String>, Integer> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<ArrayList<String>, Integer>> list =
                new LinkedList<Map.Entry<ArrayList<String>, Integer>>(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<ArrayList<String>, Integer>>() {
            public int compare(Map.Entry<ArrayList<String>, Integer> o1,
                               Map.Entry<ArrayList<String>, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        LinkedHashMap<ArrayList<String>, Integer> temp = new LinkedHashMap<ArrayList<String>, Integer>();
        for (Map.Entry<ArrayList<String>, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
}

   

