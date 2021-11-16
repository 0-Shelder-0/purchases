function moveToSignup() {
    const origin = window.location.origin;
    window.location.assign(`${origin}/signup`);
}

function moveToLogin() {
    const origin = window.location.origin;
    window.location.assign(`${origin}/login`);
}