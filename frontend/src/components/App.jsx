import { useEffect, useRef, useState } from "react";
import reactLogo from "../assets/react.svg";
import viteLogo from "/vite.svg";
import "../assets/css/App.css";

function App() {
  const [count, setCount] = useState(0);
  const effectRan = useRef(false);

  useEffect(() => {
    const fetchTransactions = async () => {
      const response = await fetch("http://localhost:8080/transactions");
      const data = await response.json();
      console.log(
        data.map(({ personalFinanceCategory, amount }) => ({
          personalFinanceCategory: personalFinanceCategory.primary,
          amount,
        }))
      );
    };

    try {
      fetchTransactions();
    } catch (error) {
      console.log(error);
    }

    effectRan.current = true; // Mark as executed

    return () => {
      effectRan.current = false; // Reset on unmount
    };
  }, []);

  return (
    <>
      <div>
        <a href="https://vite.dev" target="_blank">
          <img src={viteLogo} className="logo" alt="Vite logo" />
        </a>
        <a href="https://react.dev" target="_blank">
          <img src={reactLogo} className="logo react" alt="React logo" />
        </a>
      </div>
      <h1>Vite + React</h1>
      <div className="card">
        <button onClick={() => setCount((count) => count + 1)}>
          count is {count}
        </button>
        <p>
          Edit <code>src/App.jsx</code> and save to test HMR
        </p>
      </div>
      <p className="read-the-docs">
        Click on the Vite and React logos to learn more
      </p>
    </>
  );
}

export default App;
