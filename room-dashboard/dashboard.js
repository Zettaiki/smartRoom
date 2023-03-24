window.onload = function () {

    var connection = new WebSocket('ws://127.0.0.1:4444');

    // Get the status elements.
    const lightStatus = document.getElementsByClassName('light-status')[0];
    const rollerBlindStatus = document.getElementsByClassName('roller-blinds-status')[0];
    const controllType = document.getElementsByClassName('control-type')[0];
    const historyTable = document.getElementsByClassName('room-history')[0];


    // Get the controll buttons.
    const manualControlBtn = document.getElementsByClassName('manual-control-btn')[0];
    const lightOn = document.getElementsByClassName('lightON')[0];
    const lightOff = document.getElementsByClassName('lightOFF')[0];
    const rollerblindSlider = document.getElementsByClassName('roller-blinds-value')[0];
    const getRoomHistory = document.getElementsByClassName('get-room-history-button')[0];

    // Default room parameters.
    let manualControl = false;
    let light = false;
    let rollerBlind = 0;

    // Event listener for the controller.
    manualControlBtn.addEventListener("click", () => {
        manualControl =  manualControl ? false : true; 
        sendRoomStatusOnmanualControl(); 
    });

    lightOff.addEventListener("click", ()=>{
        light = false; 
        sendRoomStatusOnmanualControl();
    })
    
    lightOn.addEventListener("click", ()=>{
        light = true; 
        sendRoomStatusOnmanualControl();
    })

    rollerblindSlider.addEventListener("change", ()=>{
        rollerBlind = rollerblindSlider.value; 
        sendRoomStatusOnmanualControl();
    })

    getRoomHistory.addEventListener("click", () => { 
        requestRommHistory(); 
    })

    // Connection functions.
    connection.onopen = function () {
        console.log('Connected!');
        //connection.send('host connected'); // Send a message to the server to communicate connection
    };

    // Log errors
    connection.onerror = function (error) {
        console.log('WebSocket Error ' + error);
    };

    // Log messages from the server
    connection.onmessage = function (e) {
        console.log('Server: \n ' + e.data);

        let jsonMessage = JSON.parse(e.data); 
        if(jsonMessage["responseType"] == "currentStatus"){ 
            updateDisplayedValue(jsonMessage); 
        }else if(jsonMessage["responseType"] == "roomHistory"){ 
            //console.log("roomHistory");
            //console.log(jsonMessage);
            
            createHistoryTable(jsonMessage);
        }else{
            console.log("ERROR: response type not in list"); 
            console.log(jsonMessage); 
        }
    };

    function createHistoryTable(JSONstatus){
        // Delete the old table.
        historyTable.innerHTML = "";

        //create table
        let table = document.createElement("table");
        // Create table header.
        let thead = document.createElement("thead");
        let thDate = document.createElement("th");
        let thLight = document.createElement("th");
        let thRollerBlind = document.createElement("th");
        let thControllType = document.createElement("th");

        thDate.innerText = "Date";
        thLight.innerText = "Light";
        thRollerBlind.innerText = "Roller Blind";
        thControllType.innerText = "Controll Type";

        thead.appendChild(thDate);
        thead.appendChild(thControllType);
        thead.appendChild(thLight);
        thead.appendChild(thRollerBlind);

        table.appendChild(thead);
        historyTable.appendChild(table);

        let tbody = document.createElement("tbody");
        table.appendChild(tbody);

        console.log(JSONstatus.length);

        Object.keys(JSONstatus).forEach(key => {
            let row = document.createElement("tr");
            let dateCell = document.createElement("td");
            let controllTypeCell = document.createElement("td");
            let lightStatusCell = document.createElement("td");
            let rollerBlindCell = document.createElement("td"); 


            var date = new Date(key * 1000);
            var day = date.getDate();
            var month = date.getMonth() + 1;
            var year = date.getFullYear();
            var hours = date.getHours();
            var minutes = "0" + date.getMinutes();
            var seconds = "0" + date.getSeconds();
            var formattedTime = day + "/" + month + "/" + year + "  " + hours + ':' + minutes.substr(-2) + ':' + seconds.substr(-2);
            //var formattedTime = date

            dateCell.innerText = formattedTime;
            controllTypeCell.innerText = String(JSONstatus[key][0]["manualControl"]) == "true"? "Manual" : "Automatic";
            lightStatusCell.innerText = String(JSONstatus[key][0]["light"]) == "true" ? "ON" : "OFF";
            rollerBlindCell.innerText = JSONstatus[key][0]["rollerBlind"];


            row.appendChild(dateCell);
            row.appendChild(controllTypeCell);
            row.appendChild(lightStatusCell);
            row.appendChild(rollerBlindCell);

            if(key != "responseType"){ 
                tbody.appendChild(row);
            }
        });
    }


    // Update displayed values.
    function updateDisplayedValue(JSONStatus){ 
        lightString = JSONStatus["light"][0] ? "ON" : "OFF";
        controllString = JSONStatus["manualControl"][0] ? "Manual" : "Automatic";

        lightStatus.innerText = "Light = " +  lightString, 
        rollerBlindStatus.innerText = "Roller blinder = " + JSONStatus["rollerBlind"]; 
        controllType.innerText =  "Control type = " + controllString;
    }

    // Send status to the server.
    function sendRoomStatusOnmanualControl(){ 
        const obj = {
            requestType: "manual-control-btn", 
            manualControl: manualControl,
            light: light,
            rollerBlind: rollerBlind
        };

        console.log(obj); 

        // Converts JSON obj as JSON string.
        const jsonString = JSON.stringify(obj);

        // Sends JSON string to server.
            connection.send(jsonString);
    }

    // Sends history request.
    function requestRommHistory(){ 
        const obj = {
            requestType: "history"
        };

        // Converts JSON obj as JSON string.
        const jsonString = JSON.stringify(obj);
        // Sends JSON string to server.
        connection.send(jsonString);
    }
}