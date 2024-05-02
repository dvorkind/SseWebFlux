let index = (function () {

    function initSse() {
        const eventSource = new EventSource("/SseWebFlux-1.0-SNAPSHOT/sse/verification/result");

        eventSource.onmessage = e => {
            const response = e.data;
            alert(response)
        }
        eventSource.onopen = () => console.log('Connection opened');
        eventSource.onerror = () => console.log('Connection closed');
        window.onload = function () {
            let button = document.getElementById('sendButton');
            if (button) {
                button.addEventListener('click', (event) => {
                    let xhr = new XMLHttpRequest();
                    xhr.open("GET", "send", true);
                    xhr.onreadystatechange = function () {
                        if (this.readyState === 4 && this.status === 200) {
                            console.log('Запрос ушёл, работаем дальше...');
                        }
                    }
                    xhr.send();
                })
            }
        };
    }


    return {
        init: function (initObj) {
            initSse();
        }
    };
})();
