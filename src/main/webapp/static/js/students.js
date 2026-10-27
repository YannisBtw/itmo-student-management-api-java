const PAGE_SIZE = 10;

const studentsTableBody = document.getElementById("studentsTableBody");
const listMessage = document.getElementById("listMessage");
const addStudentLink = document.getElementById("addStudentLink");

const filterRoomInput = document.getElementById("filterRoom");
const filterIsuIdInput = document.getElementById("filterIsuId");
const filterGroupInput = document.getElementById("filterGroup");
const filterDormitoryInput = document.getElementById("filterDormitory");
const filterIsForeignSelect = document.getElementById("filterIsForeign");
const filterFullNameInput = document.getElementById("filterFullName");
const filterMoveInDateInput = document.getElementById("filterMoveInDate");
const filterNotesInput = document.getElementById("filterNotes");
const applyFiltersButton = document.getElementById("applyFilters");
const resetFiltersButton = document.getElementById("resetFilters");

const prevPageButton = document.getElementById("prevPage");
const nextPageButton = document.getElementById("nextPage");
const pageInfo = document.getElementById("pageInfo");

const isAdmin = Auth.isAdmin();

let currentPage = 0;
let sortBy = null;
let sortDirection = "asc";
let appliedFilters = {};


function formatDate(dateString) {
    if (!dateString) {
        return "";
    }

    const parts = dateString.split("-");
    return parts[2] + "." + parts[1] + "." + parts[0];
}


// <input type="date"> даёт YYYY-MM-DD, а фильтр на бэкенде ждёт DD-MM-YYYY
function toApiDate(dateString) {
    const parts = dateString.split("-");
    return parts[2] + "-" + parts[1] + "-" + parts[0];
}


function showListMessage(text) {
    listMessage.textContent = text;
    listMessage.hidden = false;
}


function hideListMessage() {
    listMessage.textContent = "";
    listMessage.hidden = true;
}


// Имена параметров совпадают с @QueryParam в StudentResource
function readFilters() {
    const filters = {};

    const fullName = filterFullNameInput.value.trim();
    const group = filterGroupInput.value.trim();
    const dormitory = filterDormitoryInput.value.trim();
    const roomNumber = filterRoomInput.value.trim();
    const isuId = filterIsuIdInput.value.trim();
    const moveInDate = filterMoveInDateInput.value;
    const isForeign = filterIsForeignSelect.value;
    const notes = filterNotesInput.value.trim();

    if (fullName) {
        filters.fullName = fullName;
    }

    if (group) {
        filters.group = group;
    }

    if (dormitory) {
        filters.dormitoryNumber = dormitory;
    }

    if (roomNumber) {
        filters.roomNumber = roomNumber;
    }

    if (isuId) {
        filters.isuId = isuId;
    }

    if (moveInDate) {
        filters.moveInDate = toApiDate(moveInDate);
    }

    if (isForeign !== "") {
        filters.isForeign = isForeign;
    }

    if (notes) {
        filters.notes = notes;
    }

    return filters;
}


function createCell(text) {
    const cell = document.createElement("td");
    cell.textContent = text;

    return cell;
}


function createActionButton(text, onClick) {
    const button = document.createElement("button");

    button.textContent = text;
    button.addEventListener("click", onClick);

    return button;
}


function renderStudents(students) {
    studentsTableBody.textContent = "";

    if (students.length === 0) {
        const row = document.createElement("tr");
        const cell = createCell("Студенты не найдены");

        cell.colSpan = 7;
        row.appendChild(cell);
        studentsTableBody.appendChild(row);
        return;
    }

    students.forEach(function (student) {
        const row = document.createElement("tr");

        row.appendChild(createCell(student.fullName));
        row.appendChild(createCell(student.group));
        row.appendChild(createCell(student.isuId));
        row.appendChild(createCell(student.dormitoryNumber));
        row.appendChild(createCell(student.roomNumber));
        row.appendChild(createCell(formatDate(student.moveInDate)));

        const actionsCell = document.createElement("td");
        const actions = document.createElement("div");

        actions.classList.add("actions");

        actions.appendChild(
            createActionButton("Подробнее", function () {
                window.location.href = "student-details.html?id=" + student.id;
            })
        );

        // Изменять и удалять студентов может только ADMIN (см. AuthorizationFilter)
        if (isAdmin) {
            actions.appendChild(
                createActionButton("Редактировать", function () {
                    window.location.href = "student-form.html?id=" + student.id;
                })
            );

            actions.appendChild(
                createActionButton("Удалить", async function () {
                    const shouldDelete = confirm(
                        "Удалить студента " + student.fullName + "?"
                    );

                    if (!shouldDelete) {
                        return;
                    }

                    try {
                        await deleteStudent(student.id);
                        await loadStudents();
                    } catch (error) {
                        console.error("Ошибка удаления студента:", error);
                        showListMessage(error.message);
                    }
                })
            );
        }

        actionsCell.appendChild(actions);
        row.appendChild(actionsCell);

        studentsTableBody.appendChild(row);
    });
}


function updatePagination(loadedCount) {
    pageInfo.textContent = "Страница " + (currentPage + 1);

    prevPageButton.disabled = currentPage === 0;

    // Бэкенд не возвращает общее количество, поэтому неполная страница = последняя
    nextPageButton.disabled = loadedCount < PAGE_SIZE;
}


function updateSortIndicators() {
    document.querySelectorAll("th[data-sort]").forEach(function (header) {
        const label = header.dataset.label || header.textContent;
        header.dataset.label = label;

        if (header.dataset.sort === sortBy) {
            header.textContent = label + (sortDirection === "asc" ? " ▲" : " ▼");
        } else {
            header.textContent = label;
        }
    });
}


async function loadStudents() {
    hideListMessage();

    const params = Object.assign({}, appliedFilters, {
        page: currentPage,
        size: PAGE_SIZE
    });

    if (sortBy) {
        params.sortBy = sortBy;
        params.sortDirection = sortDirection;
    }

    try {
        const students = await getAllStudents(params);

        // например, удалили последнего студента на последней странице
        if (students.length === 0 && currentPage > 0) {
            currentPage--;
            await loadStudents();
            return;
        }

        renderStudents(students);
        updatePagination(students.length);

    } catch (error) {
        console.error("Ошибка загрузки студентов:", error);
        showListMessage(error.message);
    }
}


function applyFilters() {
    appliedFilters = readFilters();
    currentPage = 0;
    loadStudents();
}


function resetFilters() {
    filterFullNameInput.value = "";
    filterGroupInput.value = "";
    filterDormitoryInput.value = "";
    filterMoveInDateInput.value = "";
    filterIsForeignSelect.value = "";
    filterNotesInput.value = "";
    filterRoomInput.value = "";
    filterIsuIdInput.value = "";

    appliedFilters = {};
    currentPage = 0;
    loadStudents();
}


function changeSort(field) {
    if (sortBy === field) {
        sortDirection = sortDirection === "asc" ? "desc" : "asc";
    } else {
        sortBy = field;
        sortDirection = "asc";
    }

    currentPage = 0;
    updateSortIndicators();
    loadStudents();
}


function initFilters() {
    applyFiltersButton.addEventListener("click", applyFilters);
    resetFiltersButton.addEventListener("click", resetFilters);

    prevPageButton.addEventListener("click", function () {
        if (currentPage > 0) {
            currentPage--;
            loadStudents();
        }
    });

    nextPageButton.addEventListener("click", function () {
        currentPage++;
        loadStudents();
    });

    document.querySelectorAll("th[data-sort]").forEach(function (header) {
        header.addEventListener("click", function () {
            changeSort(header.dataset.sort);
        });
    });

    const params = new URLSearchParams(window.location.search);

    filterFullNameInput.value = params.get("fullName") || "";
    filterGroupInput.value = params.get("group") || "";
    filterDormitoryInput.value =
        params.get("dormitoryNumber") || params.get("dormitory") || "";
    filterRoomInput.value = params.get("roomNumber") || "";
    filterIsuIdInput.value = params.get("isuId") || "";
    filterMoveInDateInput.value = params.get("moveInDate") || "";
    filterIsForeignSelect.value = params.get("isForeign") || "";
    filterNotesInput.value = params.get("notes") || "";
}


if (Auth.initPage()) {
    // Кнопка добавления нужна только администратору
    if (!isAdmin) {
        addStudentLink.style.display = "none";
    }

    initFilters();
    appliedFilters = readFilters();
    updateSortIndicators();
    loadStudents();
}
