const button = document.getElementById('inc-counter');
const counter = document.getElementById('counter');

const existingCookie = document.cookie.split("; ").find(cookie => cookie.startsWith("counterValue="));
let count = existingCookie ? existingCookie.split("=")[1] : 0;

counter.textContent = `Counter: ${count}`;

if (!existingCookie) {
    const expirationDate = new Date();
    expirationDate.setSeconds(expirationDate.getSeconds() + 10);
    console.log("There is no cookie");

    document.cookie = `counterValue=${count}; SameSite=None; Secure; expires=${expirationDate.toUTCString()}`;
}

button.addEventListener('click', function() {
    count++;
    const newExpirationDate = new Date();
    newExpirationDate.setSeconds(newExpirationDate.getSeconds() + 10);
    document.cookie = `counterValue=${count}; SameSite=None; Secure; expires=${newExpirationDate.toUTCString()}`;
    console.log(document.cookie);
    postNewCounterValue(document.cookie)
    counter.textContent = `Counter: ${count}`;
});


function postNewCounterValue(count) {
    fetch("http://localhost:8080/inccounter", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ count: count }),
    })
    .then(response => {
        console.log(response);
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        console.log(data);
    })
    .catch(error => {
        console.error("Error:", error);
    });
}

