"use client";

import { useState } from "react";
import type { AuthUser } from "@/features/auth/types";

export function useAuth() {
  const [user, setUser] = useState<AuthUser | null>(null);

  const login = (userData: AuthUser) => {
    setUser(userData);
  };

  const logout = () => {
    setUser(null);
  };

  return {
    user,
    login,
    logout,
  };
}
