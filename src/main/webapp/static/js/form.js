const studentForm = document.getElementById("studentForm");
const formMessage = document.getElementById("formMessage");
const heading = document.querySelector("h1");

const fullNameInput = document.getElementById("fullName");
const groupInput = document.getElementById("group");
const isuIdInput = document.getElementById("isuId");
const dormitoryNumberInput = document.getElementById("dormitoryNumber");
const roomNumberInput = document.getElementById("roomNumber");
const moveInDateInput = document.getElementById("moveInDate");
const isForeignInput = document.getElementById("isForeign");
const notesInput = document.getElementById("notes");


const params = new URLSearchParams(window.location.search);
const studentId = Number(params.get("id"));


// Бэкенд принимает только прошедшие даты (@Past), поэтому сегодняшний день недопустим
const yesterday = new Date();
yesterday.setDate(yesterday.getDate() - 1);

const maxDateString =
    yesterday.getFullYear() + "-"
    + String(yesterday.getMonth() + 1).padStart(2, "0") + "-"
    + String(yesterday.getDate()).padStart(2, "0");

moveInDateInput.max = maxDateString;


if (studentId) {
    document.title = "Редактирование студента";
    heading.textContent = "Редактирование студента";
}


function showFormMessage(text) {
    formMessage.textContent = text;
    formMessage.hidden = false;
}


function hideFormMessage() {
    formMessage.textContent = "";
    formMessage.hidden = true;
}


function validateFullName() {
    const fullName = fullNameInput.value.trim();
    const nameParts = fullName.split(/\s+/);

    if (nameParts.length < 2 || nameParts.some(part => part.length < 2)) {
        fullNameInput.setCustomValidity(
            "ФИО должно содержать минимум 2 слова, каждое не короче 2 символов"
        );
    } else {
        fullNameInput.setCustomValidity("");
    }
}


function getStudentFromForm() {
    return {
        fullName: fullNameInput.value.trim(),
        group: groupInput.value.trim(),
        isuId: isuIdInput.valueAsNumber,
        dormitoryNumber: dormitoryNumberInput.valueAsNumber,
        roomNumber: roomNumberInput.valueAsNumber,
        moveInDate: moveInDateInput.value,
        isForeign: isForeignInput.checked,
        notes: notesInput.value.trim()
    };
}


// Ошибки валидации с сервера (422) приходят как {поле: сообщение}; id полей формы совпадают с именами полей DTO
function showServerFieldErrors(details) {
    let firstInput = null;
    const unknownErrors = [];

    Object.keys(details).forEach(function (field) {
        const input = document.getElementById(field);

        if (input && studentForm.contains(input)) {
            input.setCustomValidity(details[field]);

            if (!firstInput) {
                firstInput = input;
            }
        } else {
            unknownErrors.push(field + ": " + details[field]);
        }
    });

    if (firstInput) {
        firstInput.reportValidity();
    }

    if (unknownErrors.length > 0) {
        showFormMessage(unknownErrors.join("; "));
    }
}


async function loadStudentForEditing() {
    if (!studentId) {
        return;
    }

    try {
        const student = await getStudentById(studentId);

        if (!student) {
            alert("Студент не найден");
            window.location.href = "index.html";
            return;
        }

        fullNameInput.value = student.fullName;
        groupInput.value = student.group;
        isuIdInput.value = student.isuId;
        dormitoryNumberInput.value = student.dormitoryNumber;
        roomNumberInput.value = student.roomNumber;
        moveInDateInput.value = student.moveInDate;
        isForeignInput.checked = student.isForeign;
        notesInput.value = student.notes;

    } catch (error) {
        console.error("Ошибка загрузки студента:", error);
        showFormMessage(error.message);
    }
}


fullNameInput.addEventListener("input", validateFullName);

// Любая правка поля сбрасывает ошибку, пришедшую с сервера
studentForm.querySelectorAll("input, textarea").forEach(function (field) {
    field.addEventListener("input", function () {
        if (field !== fullNameInput) {
            field.setCustomValidity("");
        }
    });
});


studentForm.addEventListener("submit", async function (event) {
    event.preventDefault();

    hideFormMessage();
    validateFullName();

    if (!studentForm.checkValidity()) {
        studentForm.reportValidity();
        return;
    }

    const student = getStudentFromForm();

    try {
        if (studentId) {
            student.id = studentId;
            await updateStudent(student);
        } else {
            await addStudent(student);
        }

        window.location.href = "index.html";

    } catch (error) {
        if (error.status === 409) {
            isuIdInput.setCustomValidity(
                "Студент с таким ИСУ ID уже существует"
            );

            isuIdInput.reportValidity();
            return;
        }

        if (error.status === 422 && error.details) {
            showServerFieldErrors(error.details);
            return;
        }

        console.error("Ошибка сохранения студента:", error);
        showFormMessage(error.message);
    }
});


// Форма доступна только администратору
if (Auth.initPage({ adminOnly: true })) {
    loadStudentForEditing();
}
