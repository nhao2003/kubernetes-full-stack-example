import axios from "axios";

export default axios.create({
  baseURL: 'http://student-app-api:8080/api',
  headers: {
    "Content-type": "application/json"
  }
});
