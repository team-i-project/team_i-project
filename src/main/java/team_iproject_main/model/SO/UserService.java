package team_iproject_main.model.SO;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team_iproject_main.exception.*;
import team_iproject_main.model.DAO.UserDao;
import team_iproject_main.model.DO.UserDO;
import team_iproject_main.model.Request.*;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;

@Service
@Log4j2
public class UserService {

    @Autowired
    private UserDao userDao;

    public void editor_signUp(RegisterRequest req) throws UnsupportedEncodingException {
        UserDO user = userDao.findByEmail(req.getEmail());
        UserDO userSelectByNickName = userDao.findByNickname(req.getNickname());
        UserDO userSeleteByPhoneNumber = userDao.findByPhoneNumber(req.getPhone_number());
        if (user != null) {
            throw new DuplicateEmailException();
        }
        if (userSelectByNickName != null){
            throw new DuplicateNickNameException();
        }
        if (userSeleteByPhoneNumber != null) {
            throw new DuplicatePhone_numberException();
        }

        user = new UserDO(req.getEmail(), req.getPassword(), req.getName(), req.getNickname(), req.getPhone_number(), req.getAddress(),
                req.getDetail_addr(), "편집자", req.getGender(), LocalDate.parse(req.getBirth_date()));
        userDao.createEditor(user);
    }

    public void youtuber_signUp(RegisterRequest req) {
        UserDO user = userDao.findByEmail(req.getEmail());
        UserDO userSelectByNickName = userDao.findByNickname(req.getNickname());
        UserDO userSeleteByPhoneNumber = userDao.findByPhoneNumber(req.getPhone_number());
        RegisterReqeustChannel uq = userDao.findByChannel(req.getChannel_id());
        if (user != null) {
            throw new DuplicateEmailException();
        }
        if (userSelectByNickName != null){
            throw new DuplicateNickNameException();
        }
        if (uq != null) {
            throw new DuplicateChannelException("channel_id address is already registered.");
        }
        if (userSeleteByPhoneNumber != null) {
            throw new DuplicatePhone_numberException();
        }

        user = new UserDO(req.getEmail(), req.getPassword(), req.getName(), req.getNickname(), req.getPhone_number(), req.getAddress(),
                req.getDetail_addr(), "유튜버", req.getGender(), LocalDate.parse(req.getBirth_date()), req.getChannel_id(),
                req.getSubscribe(), req.getVideo_count(), req.getView_count(), req.getChannel_name(), req.getChannel_photo());
        userDao.createYoutuber(user);
    }

    public UserDO findUser(String email){
        return userDao.findByEmail(email);
    }


    //희수
    //전체 회원 조회
    //준영 페이징 추가
    public List<UserDO> findMembers(int page, int postsPerPage) {
        int offset = (page - 1) * postsPerPage;
        return userDao.findAll(postsPerPage, offset);
    }

    public int getTotalResults() {
        return userDao.getTotalResults();
    }

    //희수
    //회원 삭제
    public void deleteMember(String email){
        userDao.deleteUserInfo(email);
    }

    //희수
    //회원 id,닉네임 검색
    public List<UserDO> findMember(UserSearchRequest userSearchRequest, int page, int postsPerPage) {
        int offset = (page - 1) * postsPerPage;
        return userDao.userFindById(userSearchRequest, postsPerPage, offset);
    }

    public int getTotalSearch(UserSearchRequest userSearchRequest) {
        return userDao.getTotalSearch(userSearchRequest);
    }

    //0506-손주현 findUser 메소드 오버로딩
    public UserDO findUser(String name, String phone_number){
        return userDao.findByNameAndPhone(name, phone_number);
    }

    //0508손주현 - checkLoginAuth 수정
    public boolean checkLoginAuth(LoginCommand login) {
        boolean result = false;
        UserDO users = userDao.findByEmail(login.getEmail());
        if(users == null){
            throw new UserNotFoundException();
        }
        else if (!users.checkPassword(login.getPassword())) {
            throw new WrongPasswordException();
        }
        if(users != null && users.checkPassword(login.getPassword())){
            result = true;
        }
        return result;
    }

    //0506-손주현
    //0508손주현 - checkFindId 수정
    public boolean checkFindId(FindIdRequest req) throws UserNotFoundException {
        boolean result = false;
        UserDO users = userDao.findByNameAndPhone(req.getName(), req.getPhone_number());
        if(users == null){
            throw new UserNotFoundException();
        }
        if(users != null && users.checkNameAndPhonenum(req.getName(), req.getPhone_number())){
            result = true;
        }
        return result;
    }

    // 비밀번호 찾기 후 비밀번호 변경
    public void changePwd(String email, String newpwd) {
        userDao.updatePassword(email, newpwd);
    }

    public void mypageupdate(UserUpdateRequest userUpdateRequest, String id) {
        userDao.updateUserInfo(userUpdateRequest,id);
    }

    public UserDO findNickname(String nickname){return userDao.findByNickname(nickname);}

    public UserDO findById(String email) {
        return userDao.findByEmail(email);
    }

    public boolean ConfirmEmail(String email) {
        boolean result = false;
        UserDO users = userDao.findByEmail(email);
        log.info("service"+users);
        if(users == null) {
            return result;
        }
        else if(!users.ConfirmEmail(email)) {
            return result;
        }
                /*if(users != null && users.ConfirmEmail(email)) {
                    result = true;
                }*/
        result = true;
        return result;
    }

    public boolean ConfirmNickname(String nickname) {
        boolean result = false;
        UserDO users = userDao.findByNickname1(nickname);
        log.info("service"+users);
        if(users == null) {
            return result;
        }
        else if(!users.ConFirmNickname(nickname)) {
            return result;
        }
                /*if(users != null && users.ConfirmEmail(email)) {
                    result = true;
                }*/
        result = true;
        return result;
    }

    public boolean ConfirmPhoneNumber(String phone_number) {
        boolean result = false;
        UserDO users = userDao.findByPhoneNumber(phone_number);

        if(users == null) {
            return result;
        }
        else if(!users.ConFirmPhoneNumber(phone_number)) {
            return result;
        }
                /*if(users != null && users.ConfirmEmail(email)) {
                    result = true;
                }*/
        result = true;
        return result;
    }

    public UserDO findByPhoneNumber(String phone_number) {
        return userDao.findByPhoneNumber(phone_number);
    }

    //0511- 손주현
    public void deleteUser(String email){
        userDao.deleteUserInfo(email);
    }
}
