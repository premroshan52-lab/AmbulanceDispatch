// =====================================================
// LOGIN
// =====================================================

document
    .getElementById("loginForm")
    .addEventListener(
        "submit",
        login
    );


// =====================================================
// LOGIN FUNCTION
// =====================================================

async function login(event) {

    event.preventDefault();

    const username =
        document.getElementById(
            "username"
        ).value.trim();

    const password =
        document.getElementById(
            "password"
        ).value;

    const button =
        document.getElementById(
            "loginButton"
        );

    if (!username || !password) {

        showMessage(
            "Please enter username and password.",
            false
        );

        return;
    }

    button.disabled = true;

    button.textContent =
        "LOGGING IN...";

    try {

        const response =
            await fetch(
                "/auth/login",
                {
                    method: "POST",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    credentials: "include",

                    body: JSON.stringify({
                        username:
                            username,

                        password:
                            password
                    })
                }
            );

        let data = {};

        try {

            data =
                await response.json();

        } catch (error) {

            data = {};
        }

        if (!response.ok) {

            throw new Error(
                data.message ||
                "Invalid username or password"
            );
        }

        showMessage(
            "Login successful. Opening dashboard...",
            true
        );

        setTimeout(
            () => {

                window.location.href =
                    "/";

            },
            700
        );

    } catch (error) {

        console.error(error);

        showMessage(
            error.message ||
            "Unable to login.",
            false
        );

        button.disabled = false;

        button.textContent =
            "LOGIN";
    }
}


// =====================================================
// SHOW MESSAGE
// =====================================================

function showMessage(
    message,
    success
) {

    const messageBox =
        document.getElementById(
            "loginMessage"
        );

    messageBox.classList.remove(
        "hidden",
        "success",
        "error"
    );

    messageBox.classList.add(
        success
            ? "success"
            : "error"
    );

    messageBox.textContent =
        message;
}