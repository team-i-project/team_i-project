function submitHandler() {
var userName = document.querySelector('#name').value;
var password = document.querySelector('#password');
var confirmPassword = document.querySelector('#confirmPassword');
var nickname = document.querySelector('#nickname');
var bithdate = document.querySelector('#birth_date');
var phonenumber = document.querySelector('#phone_number');
var address1 = document.querySelector('#address');
var addr_detail = document.querySelector('#detail_addr');
var sex = document.querySelector('#gender');
// 뒤에 value를 붙여주면 사용자가 입력한 값이 된다.
if(sex.value.length == 0) {
    alert("성별을 선택해주세요.")
    return false;
}



if(phonenumber.value.length <= 11) {
    alert("핸드폰번호를 입력하세요.")
    return false;

if(nickname.value.length > 9) {
    alert("닉네임은 9글자 이하로 입력하세요.");
    return false;
}
else if(userName.length >= 2 && password.value.length >= 4 && password.value == confirmPassword.value) {
    alert("회원가입 처리를 수행합니다.");
}
else if(password.value != confirmPassword.value){
    alert("비밀번호가 일치하지 않습니다.");
    return false;
}
else {
    alert("올바른 회원 정보를 입력하세요.");

    password.value = '';
    confirmPassword.value = '';

    return false;
// false처리하면 나중에 동작하는 default 이벤트 처리가 동작하지 않는다.
    }
}


function init() {
    var signupForm = document.querySelector('#signupForm');
    signupForm.onsubmit = submitHandler;
}

window.onload = init;


