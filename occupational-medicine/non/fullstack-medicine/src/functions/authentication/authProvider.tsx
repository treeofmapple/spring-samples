"use client";
import { useState } from "react";
import { AuthContext } from "./useAuth";
import { User } from "@/model/User";
import { loginApi } from "@/api/loginApi";

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<User | null>(null);

  const login = async (email: string, password: string): Promise<boolean> => {
    try {
      const userData = await loginApi(email, password);
      setUser(userData);
      return true;
    } catch (error) {
      console.error("An error occurred during login:", error);
      return false;
    }
  };

  const logout = () => {
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}
