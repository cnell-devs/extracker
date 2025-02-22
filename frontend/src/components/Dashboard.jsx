import { useState, useEffect, useCallback } from "react";
import { CatSelector } from "./CatSelector";
import { Sankey } from "./Sankey";
import { StatCards } from "./StatCards";
import { TransactionsList } from "./TransactionsList";

function Dashboard() {
  const [activeCat, setActiveCat] = useState("SHOW_ALL");
  const [data, setData] = useState([]); // ✅ Default as empty array
  const [isLoading, setIsLoading] = useState(false); // ✅ Track API loading state
  // 🔹 Define fetchTransactions function using useCallback
  const fetchTransactions = useCallback(async () => {
    //if (!itemLoaded) return; // ✅ Prevent fetch if item is not loaded

    setIsLoading(true); // ✅ Show loading state

    try {
      const userId = "89cffce4-e2bf-4471-ade1-e0a86d889000";
      const response = await fetch(
        `http://localhost:8080/api/transactions/${userId}`
      );
      const transactions = await response.json();

      if (!Array.isArray(transactions)) {
        throw new Error("Invalid data format from API");
      }

      // ✅ Parse data correctly

        console.log(transactions);


      setData(transactions);
    } catch (error) {
      console.error("Error fetching transactions:", error);
    } finally {
      setIsLoading(false); // ✅ Remove loading state
    }
  }, []); // ✅ Only re-create function when `itemLoaded` changes

  // 🔹 Fetch transactions when `itemLoaded` changes
  useEffect(() => {
    fetchTransactions();
  }, [fetchTransactions]);


  return (
    <div className="grid gap-6 place-items-center">
      {/* ✅ Refresh Button */}
      <button
        onClick={fetchTransactions}
        disabled={isLoading}
        className={`btn ${isLoading ? "opacity-50 cursor-not-allowed" : ""}`}
      >
        {isLoading ? "Refreshing..." : "Refresh Transactions"}
          </button>

      {data.length === 0 ? (
        <p className="text-gray-500">No Transactions Available</p>
      ) : (
        <>
          <StatCards data={data} />
          <CatSelector
                          data={data}
            activeCat={activeCat}
            setActiveCat={setActiveCat}
          />
          <Sankey data={data} active={activeCat} />
          <TransactionsList data={data} active={activeCat} />
        </>
      )}
    </div>
  );
}

export default Dashboard;
