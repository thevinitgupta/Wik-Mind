import { backendClient } from "@/lib/httpClient";
import { UserResponseDTO } from "@/types/user";
import { AxiosResponse } from "axios";

export const getCurrentUser = async () => {
  const response = await backendClient.get<UserResponseDTO>("/api/user");
  console.log("Response for user : ",response)
  return response.data;
};
