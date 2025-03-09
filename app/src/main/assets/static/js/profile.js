
function updateProfile(response){
    const profile = response.data

    const name = document.getElementById('tutor-name');
    const email = document.getElementById('tutor-email');

    const faculty = document.getElementById('faculty');
    const major = document.getElementById('major');
    const rate = document.getElementById('rate');
    const rating = document.getElementById('rating');

    name.innerHTML = profile.username;
    email.innerHTML = profile.email;
    faculty.innerHTML = profile.faculty;
    major.innerHTML = profile.major;
    rate.innerHTML = profile.rate;
    rating.innerHTML = profile.rating;
}

const profile = {
        data: {
            refresh: "",
            access: "",
            role: "",
            username: "Sewell Clark",
            email: "sewell@tutor.example.com",
            faculty: "Engineering",
            major: "Computer Science",
            rate: "",
            rating: ""
        }
}

updateProfile(profile);