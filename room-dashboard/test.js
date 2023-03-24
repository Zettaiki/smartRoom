var connection = new WebSocket('ws://127.0.0.1:4444');

const getRoomHistory = document.getElementsByClassName('get-room-history-button')[0];

console.log(manualControll);

manualControll.addEventListene("click", ()=>{
    connection.send('Ping');
}); 

connection.onopen = function () {
    console.log('Connected!');
    connection.send('Ping'); // Send the message 'Ping' to the server
};

// Log errors
connection.onerror = function (error) {
    console.log('WebSocket Error ' + error);
};

// Log messages from the server
connection.onmessage = function (e) {
    console.log('Server: ' + e.data);

};





