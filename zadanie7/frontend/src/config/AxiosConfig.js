import axios from 'axios';

export default axios.create({
  baseURL: `http://${window.location.hostname}:1323`,

  headers: {
    "Access-Control-Allow-Origin": "http://localhost:1323",
    "Content-Type": "application/json",
    "Access-Control-Allow-Methods": "GET,PUT,POST,DELETE,PATCH,OPTIONS"
  }
});
