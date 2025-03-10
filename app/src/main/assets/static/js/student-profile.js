
function updateProfile(response){
    const profile = response.data

    const name = document.getElementById('student-name');
    const email = document.getElementById('student-email');

    const faculty = document.getElementById('faculty');
    const major = document.getElementById('major');
    const year = document.getElementById('year');

    name.innerHTML = profile.username;
    email.innerHTML = profile.email;
    faculty.innerHTML = profile.faculty;
    major.innerHTML = profile.major;
    year.innerHTML = profile.year;
}

const profile = {
        data: {
            refresh: "",
            access: "",
            role: "",
            username: "Sewell Clark",
            email: "sewell@student.example.com",
            faculty: "Engineering",
            major: "Computer Science",
            year: "2024"
        }
}

// updateProfile(profile);
Android.fetchProfile();