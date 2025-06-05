package ar.com.mylback.utils.entitydtomappers.users;

import ar.com.mylback.dal.entities.users.User;
import users.UserDTO;

public class UserMapper {
    public void toDTO(User user, final UserDTO userDTO) {
        if (userDTO != null && user != null) {
            userDTO.setUuid(user.getUuid());
            userDTO.setEmail(user.getEmail());
            userDTO.setName(user.getName());
        }
    }

    public void fromDTO(UserDTO userDTO, final User user) {
        if (userDTO != null && user != null) {
            user.setUuid(userDTO.getUuid());
            user.setEmail(userDTO.getEmail());
            user.setName(userDTO.getName());
        }
    }
}
