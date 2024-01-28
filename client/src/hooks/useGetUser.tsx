import { useEffect, useState } from "react";
import UserType from "../types/entityType";
import { currentUserQuery } from "../apis/queries";

/**
 * Used to get the current user.
 */
export default function useGetUser() {
  const [user, setUser] = useState<UserType | null>(null);

  useEffect(() => {
    currentUserQuery().then(
      user => setUser(user)
    );
  }, [])

  return {
    user: user,
  };
}