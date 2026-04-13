import { io, Socket } from "socket.io-client";
import { getToken } from "./auth-store";

let socket: Socket | null = null;

export function getSocket(): Socket {
  if (socket && socket.connected) return socket;

  const token = getToken();
  const url = import.meta.env.VITE_API_URL || "http://localhost:6013";

  socket = io(`${url}/field`, {
    auth: { token },
    transports: ["websocket"],
    reconnection: true,
    reconnectionDelay: 1000,
  });

  return socket;
}

export function disconnectSocket(): void {
  if (socket) {
    socket.disconnect();
    socket = null;
  }
}
