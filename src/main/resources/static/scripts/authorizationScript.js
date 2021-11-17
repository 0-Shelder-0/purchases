function moveToSignup() {
    const origin = window.location.origin;
    window.location.assign(`${origin}/signup`);
}

function moveToLogin() {
    const origin = window.location.origin;
    window.location.assign(`${origin}/login`);
}

async function logout() {
    fetch('/logout', { method: 'POST', redirect: 'follow' })
        .then(response => {
            if (response.redirected) {
                window.location.assign(response.url);
            }
        });
}