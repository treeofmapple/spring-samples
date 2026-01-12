import { User } from "../model/User";

const BASE_API_URL = "http://127.0.0.1:8080";
const LOGIN_ENDPOINT = "/login";

export const loginApi = async (
  email: string,
  password: string
): Promise<User> => {
  try {
    const response = await fetch(`${BASE_API_URL}${LOGIN_ENDPOINT}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ email, senha: password }),
    });

    if (!response.ok) {
      if (response.status === 401) {
        throw new Error("Invalid email or password.");
      }
      throw new Error(`Login failed: ${response.statusText}`);
    }

    const userData: User = await response.json();
    return userData;
  } catch (error) {
    if (error instanceof Error) {
      throw new Error(error.message || "Unable to connect to the server.");
    }
    throw new Error("Unknown error occurred.");
  }
};
