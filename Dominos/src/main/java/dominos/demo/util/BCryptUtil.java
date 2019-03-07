package dominos.demo.util;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptUtil {
        private BCryptUtil(){};

        public static String hashPassword(String password){
            return BCrypt.hashpw(password, BCrypt.gensalt());
        }
        public static boolean checkPass(String password, String hashedPassword){
            if (BCrypt.checkpw(password, hashedPassword)) {
                return true;
            }
            return false;
        }
}
