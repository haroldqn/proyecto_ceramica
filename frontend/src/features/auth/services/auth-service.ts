import { apiRequest } from "@/lib/api-client";
import type { AuthUser } from "@/features/auth/types";

export type RegisterPayload = {
  dni: string;
  firstName: string;
  lastName: string;
  motherLastName: string;
  birthDate: string;
  email: string;
  password: string;
};

export type GoogleLoginResponse = AuthUser & {
  token: string;
};

export function registerUser(payload: RegisterPayload) {
  return apiRequest<string>("/api/auth/register", {
    method: "POST",
    body: payload,
  });
}

export function loginWithGoogle(credential: string) {
  return apiRequest<GoogleLoginResponse>("/api/auth/google", {
    method: "POST",
    body: { credential },
  });
}
