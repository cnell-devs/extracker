
import { Sankey } from "./Sankey";
import { StatCards } from "./StatCards";
import { TransactionsList } from "./TransactionsList";

function App() {


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

  const data = [
    {
      personalFinanceCategory: "TRANSPORTATION",
      amount: 6.33,
    },
    {
      personalFinanceCategory: "TRANSPORTATION",
      amount: 5.4,
    },
    {
      personalFinanceCategory: "TRAVEL",
      amount: -500,
    },
    {
      personalFinanceCategory: "FOOD_AND_DRINK",
      amount: 12,
    },
    {
      personalFinanceCategory: "FOOD_AND_DRINK",
      amount: 4.33,
    },
    {
      personalFinanceCategory: "GENERAL_MERCHANDISE",
      amount: 89.4,
    },
    {
      personalFinanceCategory: "LOAN_PAYMENTS",
      amount: 25,
    },
    {
      personalFinanceCategory: "INCOME",
      amount: -4.22 + 10000,
    },
    {
      personalFinanceCategory: "GENERAL_MERCHANDISE",
      amount: 1000,
    },
    {
      personalFinanceCategory: "TRAVEL",
      amount: 500,
    },
    {
      personalFinanceCategory: "ENTERTAINMENT",
      amount: 500,
    },
    {
      personalFinanceCategory: "GENERAL_MERCHANDISE",
      amount: 500,
    },
    {
      personalFinanceCategory: "FOOD_AND_DRINK",
      amount: 500,
    },
    {
      personalFinanceCategory: "GENERAL_MERCHANDISE",
      amount: 2078.5,
    },
    {
      personalFinanceCategory: "PERSONAL_CARE",
      amount: 78.5,
    },
    {
      personalFinanceCategory: "GENERAL_SERVICES",
      amount: 5850,
    },
  ];

  const dataMap = new Map();
  data.forEach((data) => {
    const catTotal = dataMap.get(data.personalFinanceCategory);
    dataMap.set(
      data.personalFinanceCategory,
      catTotal ? catTotal + data.amount : data.amount
    );
  });

  return (
   dataMap && <div className="grid border-2 place-items-center">
      <StatCards dataMap={dataMap} />
      <Sankey dataMap={dataMap} />
      <TransactionsList data={data} />
    </div >
  );
}

export default App;
