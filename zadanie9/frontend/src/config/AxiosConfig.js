import axios from 'axios';

export default axios.create({
    baseURL: `https://the-gadget-zone-server.azurewebsites.net`,

    headers: {
        'Access-Control-Allow-Origin': 'http://localhost:1323, https://the-gadget-zone-server.azurewebsites.net',
        'Content-Type': 'application/json',
        'Access-Control-Allow-Methods': 'GET,PUT,POST,DELETE,PATCH,OPTIONS',
    },
});
