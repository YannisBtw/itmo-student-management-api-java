const loginForm = document.getElementById("loginForm");
const registerForm = document.getElementById("registerForm");
const showRegisterButton = document.getElementById("showRegister");
const showLoginButton = document.getElementById("showLogin");
const authTitle = document.getElementById("authTitle");
const authMessage = document.getElementById("authMessage");


const KNOWN_MESSAGES = {
    "Invalid credentials": "Неверный логин или пароль",
    "Username already taken": "Имя пользователя уже занято",
    "Email already registered": "Этот email уже зарегистрирован"
};


function showAuthMessage(text) {
    authMessage.textContent = text;
    authMessage.hidden = false;
}


function hideAuthMessage() {
    authMessage.textContent = "";
    authMessage.hidden = true;
}


function describeError(error) {
    if (error.status === 422 && error.details) {
        return Object.keys(error.details)
            .map(function (field) {
                return field + ": " + error.details[field];
            })
            .join("; ");
    }

    return KNOWN_MESSAGES[error.message] || error.message;
}


function showForm(mode) {
    const isRegister = mode === "register";

    loginForm.hidden = isRegister;
    registerForm.hidden = !isRegister;
    authTitle.textContent = isRegister ? "Регистрация" : "Вход";
    document.title = authTitle.textContent;

    hideAuthMessage();
}


loginForm.addEventListener("submit", async function (event) {
    event.preventDefault();
    hideAuthMessage();

    try {
        await Auth.login(
            document.getElementById("loginUsername").value.trim(),
            document.getElementById("loginPassword").value
        );

        window.location.replace("index.html");

    } catch (error) {
        showAuthMessage(describeError(error));
    }
});


registerForm.addEventListener("submit", async function (event) {
    event.preventDefault();
    hideAuthMessage();

    const username = document.getElementById("registerUsername").value.trim();
    const email = document.getElementById("registerEmail").value.trim();
    const password = document.getElementById("registerPassword").value;

    try {
        await Auth.register(username, email, password);

        // после успешной регистрации сразу входим
        await Auth.login(username, password);

        window.location.replace("index.html");

    } catch (error) {
        showAuthMessage(describeError(error));
    }
});


showRegisterButton.addEventListener("click", function () {
    showForm("register");
});

showLoginButton.addEventListener("click", function () {
    showForm("login");
});


if (Auth.isLoggedIn()) {
    window.location.replace("index.html");
}
