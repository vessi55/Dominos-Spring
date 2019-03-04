package dominos.demo.model.restaurants;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
<<<<<<< HEAD

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String city;
    private String address;
=======
>>>>>>> 0bc202a102fe9419aad43e924cfe7633b097da3b

//
//@Getter
//@Setter
//@NoArgsConstructor
//@Entity
//@Table(name = "restaurants")
//public class Restaurant {
//    @Id
//    @GeneratedValue(strategy =  GenerationType.IDENTITY)
//    private long id;
//    private String city;
//    private String address;
//
//}
