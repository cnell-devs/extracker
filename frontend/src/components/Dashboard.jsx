import {  useState } from "react";
import { CatSelector } from "./CatSelector";
import { Sankey } from "./Sankey";
import { StatCards } from "./StatCards";
import { TransactionsList } from "./TransactionsList";
import dataExample from "./data-example.json";

function Dashboard() {
  const [activeCat, setActiveCat] = useState("SHOW_ALL");


  // useEffect(() => {
  //   const fetchTransactions = async () => {
  //     const response = await fetch("http://localhost:8080/transactions");
  //     const data = await response.json();
  //     const parsedData = (
  //       data.map(({ personalFinanceCategory, amount }) => ({
  //         personalFinanceCategory: personalFinanceCategory.primary,
  //         amount,
  //       }))
  //     );
  //     setTransactions(parsedData)
  //     console.log(parsedData);

  //   };

  //   try {
  //     fetchTransactions();
  //   } catch (error) {
  //     console.log(error);
  //   }

  //   effectRan.current = true; // Mark as executed

  //   return () => {
  //     effectRan.current = false; // Reset on unmount
  //   };
  // }, []);

  const data = dataExample.transactions.map(
    ({ amount, personalFinanceCategory }) => ({
      amount,
      personalFinanceCategory,
    })
  );
  const income = dataExample.transactions.filter(
    (data) => data.personalFinanceCategory.primary == "INCOME"
  );
  const totalIncome = income
    .map((transaction) => transaction.amount)
    .reduce((acc, curr) => acc + curr);


  const dataMap = new Map();
  data
    .filter((data) =>
      activeCat == "SHOW_ALL"
        ? data.personalFinanceCategory
        : data.personalFinanceCategory.primary == activeCat
    )
    .forEach((data) => {
      // console.log(data);
      // console.log(activeCat);

      if (activeCat == "SHOW_ALL") {
        const catTotal = dataMap.get(data.personalFinanceCategory.primary);

        dataMap.set(
          data.personalFinanceCategory.primary,
          catTotal ? catTotal + data.amount : data.amount
        );
      } else {
          const catTotal = dataMap.get(data.personalFinanceCategory.detailed);
          dataMap.set("INCOME", totalIncome)
        dataMap.set(
          data.personalFinanceCategory.detailed,
          catTotal ? catTotal + data.amount : data.amount
        );
      }
    });
  //     setDataMap(dataMap);
  //   }, [activeCat]);



  return (
    dataMap && (
      <div className="grid gap-6 place-items-center">
        <StatCards data={data} />
        <CatSelector
          categories={Array.from(
            new Set(data.map((cat) => cat.personalFinanceCategory.primary))
          )}
          activeCat={activeCat}
          setActiveCat={setActiveCat}
        />
        <Sankey dataMap={dataMap} active={activeCat} />
        <TransactionsList data={dataExample.transactions} active={activeCat} />
      </div>
    )
  );
}

export default Dashboard;
