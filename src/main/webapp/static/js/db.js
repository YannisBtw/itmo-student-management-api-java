// Относительный путь: работает при любом context root (например, /web-lab-3/)
const API_URL = "api/students";


// params: fullName, isuId, group, dormitoryNumber, roomNumber, moveInDate (DD-MM-YYYY),
//         isForeign, notes, sortBy, sortDirection, page, size
async function getAllStudents(params = {}) {
    const query = new URLSearchParams(params).toString();

    const response = await Auth.apiFetch(
        API_URL + (query ? "?" + query : "")
    );

    if (!response.ok) {
        throw await Auth.buildError(
            response,
            "Ошибка загрузки студентов: " + response.status
        );
    }

    return response.json();
}


async function getStudentById(id) {
    const response = await Auth.apiFetch(API_URL + "/" + id);

    if (response.status === 404) {
        return null;
    }

    if (!response.ok) {
        throw await Auth.buildError(
            response,
            "Ошибка загрузки студента: " + response.status
        );
    }

    return response.json();
}


async function addStudent(student) {
    const response = await Auth.apiFetch(API_URL, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(student)
    });

    if (!response.ok) {
        throw await Auth.buildError(
            response,
            "Ошибка добавления студента"
        );
    }

    return response.json();
}


async function updateStudent(student) {
    const studentId = student.id;

    const data = {
        fullName: student.fullName,
        group: student.group,
        isuId: student.isuId,
        dormitoryNumber: student.dormitoryNumber,
        roomNumber: student.roomNumber,
        moveInDate: student.moveInDate,
        isForeign: student.isForeign,
        notes: student.notes
    };

    const response = await Auth.apiFetch(
        API_URL + "/" + studentId,
        {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        }
    );

    if (!response.ok) {
        throw await Auth.buildError(
            response,
            "Ошибка обновления студента"
        );
    }

    return response.json();
}


async function deleteStudent(id) {
    const response = await Auth.apiFetch(
        API_URL + "/" + id,
        {
            method: "DELETE"
        }
    );

    if (!response.ok) {
        throw await Auth.buildError(
            response,
            "Ошибка удаления студента: " + response.status
        );
    }
}
