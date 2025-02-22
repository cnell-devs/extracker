import { useState } from "react";
import Dashboard from "./Dashboard";
import Link from "./Link";

function App() {
  const [itemLoaded, setItemLoaded] = useState(false)
  return (
    <>
      <Link setItemLoaded={setItemLoaded} />;
      <Dashboard itemLoaded={itemLoaded} />
    </>
  );
}

export default App;
