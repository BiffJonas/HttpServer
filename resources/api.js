async function getAllItems() {
    
    response = await fetch("http://localhost:8080/api/");
    data = await response.json();
    console.log(data);
}

const getAllButton  = document.getElementById('get-all-button');

getAllButton.addEventListener('click', getAllItems)

