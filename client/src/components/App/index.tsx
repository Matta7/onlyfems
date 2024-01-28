import { useState } from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";

import '../../styles/App.css';
import clientPath from '../../utils/clientPath';
import NoAuthPage from "../NoAuthPage";
import { QueryClient, QueryClientProvider } from 'react-query';
import { SnackMessageType } from "../../types/entityType";
import Profile from "../Profile";
import Administration from "../Administration";
import AdminUsers from "../Administration/AdminUsers";
import AdminImages from "../Administration/AdminImages";
import Gallery from "../Gallery";
import NavbarContainer from "../NavbarContainer";

const App = () => {
  const queryClient = new QueryClient();
  const [snackMessage, setSnackMessage] = useState<SnackMessageType | null>(null);
  
  return (
    <QueryClientProvider client={queryClient}>
      <div className="app">
        <BrowserRouter>
          <Routes>
            <Route
              path={clientPath.HOME} 
              element={
                <NoAuthPage
                  snackMessage={snackMessage}
                  setSnack={setSnackMessage}
                />
              } 
            />
            <Route path={clientPath.HOME} element={<NavbarContainer snackMessage={snackMessage} setSnack={setSnackMessage} />}>
              <Route index path={clientPath.GALLERY} element={<Gallery />} />
                <Route path={clientPath.VIP_GALLERY} element={<Gallery vipContent />} />
                <Route path={clientPath.PROFILE} element={<Profile setSnack={setSnackMessage} />}/>
                <Route path={clientPath.ADMIN} element={<Administration />}>
                  <Route path={clientPath.ADMIN_USERS} element={<AdminUsers setSnack={setSnackMessage} />} />
                  <Route path={clientPath.ADMIN_IMAGES} element={<AdminImages setSnack={setSnackMessage} />} />
                </Route>
            </Route>
          </Routes>
        </BrowserRouter>
      </div>
    </QueryClientProvider>
  )
};

export default App;