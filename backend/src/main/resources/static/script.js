const API_BASE = 'http://localhost:8080/api';
let currentUser = null;
let currentRoomId = null;
let studentToRemoveId = null;

// ========== INITIALIZATION ==========
document.addEventListener('DOMContentLoaded', function() {
    checkLoginStatus();

    const path = window.location.pathname.toLowerCase();

    if (path.includes('admin-dashboard')) {
        loadAdminDashboard();
    } else if (path.includes('student-dashboard')) {
        // Student dashboard has its own init in the inline script
    } else if (path.includes('students.html')) {
        loadStudents();
    } else if (path.includes('rooms.html')) {
        // rooms.html has its own init in the inline script
    }
});

// ========== AUTHENTICATION ==========

async function login(event) {
    event.preventDefault();

    const username = (document.getElementById('username').value || '').trim();
    const password = (document.getElementById('password').value || '').trim();

    try {
        const response = await fetch(`${API_BASE}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });

        let result = null;
        try { result = await response.json(); } catch (e) { /* ignore */ }

        if (response.ok && result) {
            const user = result.user ? result.user : result;
            const role = result.role ? result.role : (user ? user.role : null);

            if (user) {
                localStorage.setItem('currentUser', JSON.stringify(user));
                currentUser = user;
            }

            if (role === 'ADMIN') {
                window.location.href = 'admin-dashboard.html';
                return;
            } else if (role === 'STUDENT') {
                // FIXED: Use correct casing to match actual filename
                window.location.href = 'Student-Dashboard.html';
                return;
            }

            window.location.reload();
            return;
        }

        if (result && result.message) {
            alert('Login failed: ' + result.message);
        } else {
            alert('Login failed. Server returned status ' + (response.status || 'unknown'));
        }
    } catch (error) {
        console.error('Error during login:', error);
        alert('Error during login: ' + error.message);
    }
}

async function registerStudent(event) {
    event.preventDefault();

    const student = {
        username: document.getElementById('regUsername').value,
        password: document.getElementById('regPassword').value,
        email: document.getElementById('regEmail').value,
        regNo: document.getElementById('regNumber').value,
        phoneNumber: document.getElementById('regPhone').value,
        yearOfStudy: parseInt(document.getElementById('regYear').value),
        attendancePercentage: parseFloat(document.getElementById('regAttendance').value),
        studentType: document.getElementById('regStudentType').value
    };

    try {
        const response = await fetch(`${API_BASE}/auth/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(student)
        });

        const result = await response.json();

        if (result.success) {
            if (result.user) {
                localStorage.setItem('currentUser', JSON.stringify(result.user));
                window.location.href = 'Student-Dashboard.html';
                return;
            }
            alert('Registration successful! You can now login.');
            if (typeof hideRegisterForm === 'function') hideRegisterForm();
        } else {
            alert('Registration failed: ' + result.message);
        }
    } catch (error) {
        console.error('Error during registration:', error);
        alert('Error during registration: ' + error.message);
    }
}

function checkLoginStatus() {
    const userData = localStorage.getItem('currentUser');
    if (userData) {
        currentUser = JSON.parse(userData);

        // Update welcome messages
        const welcomeElement = document.getElementById('adminWelcome') ||
                              document.getElementById('studentWelcome');
        if (welcomeElement) {
            welcomeElement.textContent = `Welcome, ${currentUser.username}!`;
        }

        // Redirect if on login page but already logged in
        const path = window.location.pathname.toLowerCase();
        if (path.endsWith('index.html') || path === '/') {
            if (currentUser.role === 'ADMIN') {
                window.location.href = 'admin-dashboard.html';
            } else if (currentUser.role === 'STUDENT') {
                window.location.href = 'Student-Dashboard.html';
            }
        }
    } else {
        // Redirect to login if trying to access protected pages
        const path = window.location.pathname.toLowerCase();
        if (path.includes('admin-dashboard') || path.includes('student-dashboard')) {
            window.location.href = 'index.html';
        }
    }
}

function logout() {
    localStorage.removeItem('currentUser');
    currentUser = null;
    window.location.href = 'index.html';
}

// ========== ADMIN DASHBOARD ==========

async function loadAdminDashboard() {
    if (!currentUser || currentUser.role !== 'ADMIN') {
        window.location.href = 'index.html';
        return;
    }

    try {
        const [students, rooms] = await Promise.all([
            fetch(`${API_BASE}/students`).then(r => r.json()),
            fetch(`${API_BASE}/rooms`).then(r => r.json())
        ]);

        // Update stats
        const el = (id) => document.getElementById(id);
        if (el('totalStudentsAdmin')) el('totalStudentsAdmin').textContent = students.length;
        if (el('allocatedStudents')) el('allocatedStudents').textContent = students.filter(s => s.allocatedRoom).length;
        if (el('unallocatedStudents')) el('unallocatedStudents').textContent = students.filter(s => !s.allocatedRoom).length;
        if (el('totalRoomsAdmin')) el('totalRoomsAdmin').textContent = rooms.length;

        loadRoomsForAdmin(rooms);
        loadStudentsForAdmin(students);
        loadHostelsForRoomForm();

    } catch (error) {
        console.error('Error loading admin dashboard:', error);
    }
}

function loadRoomsForAdmin(rooms) {
    const container = document.getElementById('roomsContainer');
    if (!container) return;

    container.innerHTML = '';

    if (rooms.length === 0) {
        container.innerHTML = '<div class="col-12 text-center text-muted py-3">No rooms found. Add a room to get started.</div>';
        return;
    }

    rooms.forEach(room => {
        const occupied = room.students ? room.students.length : 0;
        const available = room.capacity - occupied;
        const isAvailable = available > 0;
        const hostelName = room.hostel ? (room.hostel.hostelName || '') : '';
        const sharingLabel = room.sharingType === 'SINGLE' ? 'Single' :
                             room.sharingType === 'DOUBLE_SHARING' ? 'Double' :
                             room.sharingType === 'THREE_SHARING' ? 'Triple' : room.sharingType;

        const card = `<div class="col-md-4 col-lg-3 animate-in">
            <div class="room-card">
                <div class="d-flex justify-content-between align-items-start mb-2">
                    <div class="room-number">Room ${escapeHtml(room.roomNo)}</div>
                    ${isAvailable
                        ? '<span class="badge bg-success">Available</span>'
                        : '<span class="badge bg-danger">Full</span>'
                    }
                </div>
                <div class="room-meta">
                    <span>${sharingLabel}</span>
                    <span>${room.acType === 'AC' ? '❄️ AC' : '🌀 Non-AC'}</span>
                    <span>${room.bathroomType === 'ATTACHED' ? '🚿 Attached' : '🏢 Common'}</span>
                </div>
                ${hostelName ? `<div class="room-meta mt-1"><span>🏠 ${escapeHtml(hostelName)}</span></div>` : ''}
                <div class="d-flex justify-content-between mt-2" style="font-size: 0.8125rem; color: var(--clr-text-muted);">
                    <span>Capacity: ${room.capacity}</span>
                    <span>Occupied: ${occupied}/${room.capacity}</span>
                </div>
                <div class="d-flex gap-2 mt-3">
                    ${isAvailable
                        ? `<button class="btn btn-primary btn-sm" onclick="showAllocationModal(${room.id}, '${escapeHtml(room.roomNo)}')">Allocate</button>`
                        : ''
                    }
                    <button class="btn btn-outline-danger btn-sm" onclick="deleteRoom(${room.id})">Delete</button>
                </div>
            </div>
        </div>`;
        container.innerHTML += card;
    });
}

function loadStudentsForAdmin(students) {
    const table = document.getElementById('studentsTable');
    if (!table) return;

    table.innerHTML = '';

    students.forEach(student => {
        const name = student.name || student.username || '—';
        const username = student.username || '—';
        const regNo = student.regNo || '—';
        const email = student.email || '—';
        const year = (student.yearOfStudy != null) ? student.yearOfStudy : '—';
        const attendance = (student.attendancePercentage != null) ? `${student.attendancePercentage}%` : '—';
        const type = student.studentType || 'UG';

        const roomInfo = (student.allocatedRoom && student.allocatedRoom.roomNo)
            ? `<span class="badge bg-success">${escapeHtml(student.allocatedRoom.roomNo)}</span>`
            : '<span class="badge bg-warning">Not Allocated</span>';

        const actions = (student.allocatedRoom && student.allocatedRoom.roomNo)
            ? `<button class="btn btn-danger btn-sm" onclick="showRemoveStudentModal(${student.id}, '${escapeHtml(username)}')">Remove</button>`
            : '<span class="text-muted" style="font-size: 0.8125rem;">—</span>';

        const viewBtn = `<button class="btn btn-info btn-sm ms-1" onclick="showStudentRequestModal(${student.id})">View</button>`;

        // Column order matches header: Name, Username, RegNo, Email, Year, Attendance, Type, Room, Actions
        const row = `<tr id="student-row-${student.id}">
            <td>${escapeHtml(name)}</td>
            <td>${escapeHtml(username)}</td>
            <td>${escapeHtml(regNo)}</td>
            <td>${escapeHtml(email)}</td>
            <td>${escapeHtml(year)}</td>
            <td>${escapeHtml(attendance)}</td>
            <td><span class="badge bg-primary">${escapeHtml(type)}</span></td>
            <td class="student-room-cell">${roomInfo}</td>
            <td class="student-actions-cell">${actions} ${viewBtn}</td>
        </tr>`;

        table.innerHTML += row;
    });
}

// ========== UTILITY ==========

function escapeHtml(unsafe) {
    if (unsafe === undefined || unsafe === null) return '';
    return String(unsafe)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#039;');
}

// ========== ROOM MANAGEMENT ==========

function showAddRoomForm() {
    try { loadHostelsForRoomForm(); } catch (e) { /* ignore */ }
    document.getElementById('addRoomForm').style.display = 'block';
}

function hideAddRoomForm() {
    document.getElementById('addRoomForm').style.display = 'none';
    document.getElementById('roomForm').reset();
}

function showAllocationModal(roomId, roomNo) {
    currentRoomId = roomId;
    document.getElementById('selectedRoomInfo').textContent = roomNo;
    loadUnallocatedStudents();
    const modal = new bootstrap.Modal(document.getElementById('allocationModal'));
    modal.show();
}

function showRemoveStudentModal(studentId, studentName) {
    studentToRemoveId = studentId;
    document.getElementById('studentToRemoveName').textContent = studentName;
    const modal = new bootstrap.Modal(document.getElementById('removeStudentModal'));
    modal.show();
}

function confirmRemoveStudent() {
    if (studentToRemoveId) {
        removeStudentFromRoom(studentToRemoveId);
        bootstrap.Modal.getInstance(document.getElementById('removeStudentModal')).hide();
    }
}

async function addRoom(event) {
    event.preventDefault();

    const room = {
        roomNo: document.getElementById('roomNo').value,
        sharingType: document.getElementById('sharingType').value,
        bathroomType: document.getElementById('bathroomType').value,
        acType: document.getElementById('acType').value,
        capacity: parseInt(document.getElementById('capacity').value)
    };

    // Attach hostel if selected
    const hostelSelect = document.getElementById('hostelSelect');
    if (hostelSelect && hostelSelect.value) {
        room.hostel = { id: parseInt(hostelSelect.value) };
    }

    try {
        const response = await fetch(`${API_BASE}/rooms`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(room)
        });

        if (response.ok) {
            alert('Room added successfully!');
            hideAddRoomForm();
            // Reload the page to refresh data
            if (window.location.pathname.toLowerCase().includes('rooms.html')) {
                if (typeof loadRoomsPage === 'function') loadRoomsPage();
            } else {
                loadAdminDashboard();
            }
        } else {
            const err = await response.text();
            alert('Error adding room: ' + err);
        }
    } catch (error) {
        console.error('Error adding room:', error);
        alert('Error adding room: ' + error.message);
    }
}

async function deleteRoom(roomId) {
    if (!confirm('Are you sure you want to delete this room?')) return;

    try {
        const response = await fetch(`${API_BASE}/rooms/${roomId}`, { method: 'DELETE' });
        if (response.ok) {
            alert('Room deleted successfully!');
            if (window.location.pathname.toLowerCase().includes('rooms.html')) {
                if (typeof loadRoomsPage === 'function') loadRoomsPage();
            } else {
                loadAdminDashboard();
            }
        }
    } catch (error) {
        console.error('Error deleting room:', error);
        alert('Error deleting room: ' + error.message);
    }
}

// ========== STUDENT MANAGEMENT ==========

async function loadStudents() {
    try {
        const response = await fetch(`${API_BASE}/students`);
        const students = await response.json();

        const table = document.getElementById('studentsTable');
        if (!table) return;

        table.innerHTML = '';

        students.forEach(student => {
            const roomInfo = (student.allocatedRoom && student.allocatedRoom.roomNo)
                ? `<span class="badge bg-success">${escapeHtml(student.allocatedRoom.roomNo)}</span>`
                : '<span class="badge bg-warning">Not Allocated</span>';

            const row = `<tr>
                <td>${escapeHtml(student.username)}</td>
                <td>${escapeHtml(student.regNo)}</td>
                <td>${escapeHtml(student.email)}</td>
                <td>${student.yearOfStudy || '—'}</td>
                <td>${student.attendancePercentage != null ? student.attendancePercentage + '%' : '—'}</td>
                <td>${roomInfo}</td>
                <td>
                    <button class="btn btn-danger btn-sm" onclick="removeStudent(${student.id})">Remove</button>
                </td>
            </tr>`;
            table.innerHTML += row;
        });
    } catch (error) {
        console.error('Error loading students:', error);
    }
}

async function removeStudent(studentId) {
    if (!confirm('Are you sure you want to remove this student?')) return;

    try {
        const response = await fetch(`${API_BASE}/students/${studentId}`, { method: 'DELETE' });
        if (response.ok) {
            alert('Student removed successfully!');
            loadStudents();
        }
    } catch (error) {
        console.error('Error removing student:', error);
        alert('Error removing student: ' + error.message);
    }
}

async function loadUnallocatedStudents() {
    try {
        const response = await fetch(`${API_BASE}/students/unallocated`);
        const students = await response.json();

        const select = document.getElementById('studentSelect');
        if (!select) return;

        select.innerHTML = '<option value="">Select a student</option>';
        students.forEach(student => {
            select.innerHTML += `<option value="${student.id}">${escapeHtml(student.username)} (${escapeHtml(student.regNo)})</option>`;
        });
    } catch (error) {
        console.error('Error loading unallocated students:', error);
    }
}

async function allocateStudentToRoom() {
    const studentSelect = document.getElementById('studentSelect');
    const studentId = studentSelect ? studentSelect.value : null;

    if (!studentId) { alert('Please select a student'); return; }
    if (!currentRoomId) { alert('No room selected for allocation'); return; }

    try {
        const response = await fetch(`${API_BASE}/students/${studentId}/allocate/room/${currentRoomId}`, {
            method: 'POST'
        });

        if (response.ok) {
            const updatedStudent = await response.json();
            const roomNo = updatedStudent.allocatedRoom ? updatedStudent.allocatedRoom.roomNo : '';
            alert(`Room ${roomNo} allocated successfully!`);

            // Close modal
            bootstrap.Modal.getInstance(document.getElementById('allocationModal')).hide();

            // Update table row if visible
            const row = document.getElementById(`student-row-${updatedStudent.id}`);
            if (row) {
                const roomCell = row.querySelector('.student-room-cell');
                const actionsCell = row.querySelector('.student-actions-cell');
                if (roomCell) roomCell.innerHTML = updatedStudent.allocatedRoom
                    ? `<span class="badge bg-success">${escapeHtml(updatedStudent.allocatedRoom.roomNo)}</span>`
                    : '<span class="badge bg-warning">Not Allocated</span>';
                if (actionsCell) actionsCell.innerHTML = updatedStudent.allocatedRoom
                    ? `<button class="btn btn-danger btn-sm" onclick="showRemoveStudentModal(${updatedStudent.id}, '${escapeHtml(updatedStudent.username)}')">Remove</button> <button class="btn btn-info btn-sm ms-1" onclick="showStudentRequestModal(${updatedStudent.id})">View</button>`
                    : '<span class="text-muted">—</span>';
            }

            // Refresh data
            if (window.location.pathname.toLowerCase().includes('rooms.html')) {
                if (typeof loadRoomsPage === 'function') loadRoomsPage();
            } else {
                loadAdminDashboard();
            }
        } else {
            const error = await response.text();
            alert('Error: ' + error);
        }
    } catch (error) {
        console.error('Error allocating room:', error);
        alert('Error allocating room: ' + error.message);
    }
}

async function removeStudentFromRoom(studentId) {
    if (!confirm('Remove this student from their room?')) return;

    try {
        const response = await fetch(`${API_BASE}/admin/student/${studentId}`, { method: 'DELETE' });
        if (response.ok) {
            alert('Student removed from room successfully!');
            loadAdminDashboard();
        }
    } catch (error) {
        console.error('Error removing student from room:', error);
        alert('Error: ' + error.message);
    }
}

// ========== STUDENT REQUEST MODAL ==========

async function showStudentRequestModal(studentId) {
    try {
        const response = await fetch(`${API_BASE}/students/${studentId}`);
        if (!response.ok) { alert('Failed to load student details'); return; }
        const student = await response.json();

        const body = document.getElementById('studentRequestModalBody');
        if (!body) return;

        const val = (v) => v ? escapeHtml(v) : '<em style="color: var(--clr-text-muted);">—</em>';

        body.innerHTML = `
            <p><strong style="color: var(--clr-text-secondary);">Name:</strong> <span style="color: var(--clr-text);">${val(student.username)}</span></p>
            <p><strong style="color: var(--clr-text-secondary);">Reg No:</strong> <span style="color: var(--clr-text);">${val(student.regNo)}</span></p>
            <hr>
            <p><strong style="color: var(--clr-text-secondary);">Requested Hostel:</strong> <span style="color: var(--clr-text);">${val(student.requestedHostel)}</span></p>
            <p><strong style="color: var(--clr-text-secondary);">Room Type:</strong> <span style="color: var(--clr-text);">${val(student.requestedRoomSharing)}</span></p>
            <p><strong style="color: var(--clr-text-secondary);">AC:</strong> <span style="color: var(--clr-text);">${val(student.requestedAc)}</span></p>
            <p><strong style="color: var(--clr-text-secondary);">Bathroom:</strong> <span style="color: var(--clr-text);">${val(student.requestedBathroom)}</span></p>
            <p><strong style="color: var(--clr-text-secondary);">Status:</strong> <span style="color: var(--clr-text);">${val(student.requestStatus || 'PENDING')}</span></p>
        `;

        const modal = new bootstrap.Modal(document.getElementById('studentRequestModal'));
        modal.show();
    } catch (error) {
        console.error('Error loading student request:', error);
        alert('Error: ' + error.message);
    }
}

// ========== HOSTEL LOADER FOR ROOM FORMS ==========

async function loadHostelsForRoomForm() {
    try {
        const response = await fetch(`${API_BASE}/hostels`);
        if (!response.ok) return;
        const hostels = await response.json();
        const select = document.getElementById('hostelSelect');
        if (!select) return;
        select.innerHTML = '<option value="">Select Hostel</option>';
        hostels.forEach(h => {
            const name = h.hostelName || h.name || ('Hostel ' + h.id);
            const type = (h.hostelType || '').toString();
            const label = type ? `${name} (${type})` : name;
            select.innerHTML += `<option value="${h.id}">${escapeHtml(label)}</option>`;
        });
    } catch (e) {
        console.error('Failed to load hostels:', e);
    }
}