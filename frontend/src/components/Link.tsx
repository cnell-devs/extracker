import React, { useCallback, useState } from "react";

import { usePlaidLink, PlaidLinkOnSuccess, PlaidLinkOnSuccessMetadata } from "react-plaid-link";


// Define the account type
interface LinkedAccount {
  accessToken: string;
  account: PlaidLinkOnSuccessMetadata;
}

const SimplePlaidLink = ({setItemLoaded}) => {
  const [token, setToken] = useState(null);
  const [accounts, setAccounts] = useState<LinkedAccount[]>([]);

  // get link_token from your server when component mounts
  React.useEffect(() => {
    const createLinkToken = async () => {
      const response = await fetch("http://localhost:8080/create-link-token", {
        headers: {
          "Content-Type": "application/json",
        },
        method: "GET",
      });
      const { linkToken } = await response.json();

      setToken(linkToken);
    };
    createLinkToken();
  }, []);

  const onSuccess =
    useCallback <
    PlaidLinkOnSuccess >
    (async (publicToken, metadata) => {
      // send public_token to your server
      const request = fetch("http://localhost:8080/exchange-token", {
        headers: {
          "Content-Type": "application/json", // ✅ Required for JSON body
        },
        method: "POST",
        body: JSON.stringify({ publicToken: publicToken }),
      });
      const response = await request;
      console.log(request);

      const accessTokenResponse = await response.json();
      console.log(accessTokenResponse);

      localStorage.setItem("accessToken", accessTokenResponse.accessToken);
      // https://plaid.com/docs/api/tokens/#token-exchange-flow
      console.log(metadata);
      const newAccount = {
        accessToken: accessTokenResponse.accessToken,
        account: metadata,
      };
      setAccounts([...accounts, newAccount]);
      setItemLoaded(true)
    },
    []);

  const { open, ready } = usePlaidLink({
    token,
    onSuccess,
    // onEvent
    // onExit
  });

  return (
    <>
      <button onClick={() => open()} disabled={!ready} className="btn">
        Connect a bank account
      </button>
      <div>
        {accounts.length > 0 && (
          <p>Accounts Linked: {accounts.length}</p>
        )}
        {accounts.map((account, i) => <p key={i}>{}</p>)}
      </div>
    </>
  );
};

export default SimplePlaidLink;
