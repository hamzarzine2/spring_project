package com.example.gateway;

import com.example.gateway.data.AuthenticationProxy;
import com.example.gateway.data.InvestorProxy;
import com.example.gateway.data.OrderProxy;
import com.example.gateway.data.WalletProxy;
import com.example.gateway.exceptions.BadRequestException;
import com.example.gateway.exceptions.ConflictException;
import com.example.gateway.exceptions.NotFoundException;
import com.example.gateway.exceptions.UnauthorizedException;
import com.example.gateway.models.*;
import feign.FeignException;
import org.springframework.stereotype.Service;

@Service
public class GatewayService {

  private final AuthenticationProxy authenticationProxy;
  private final InvestorProxy investorProxy;
  private final OrderProxy orderProxy;
  private final WalletProxy walletProxy;

  /**
   * Constructs a new GatewayService with the specified proxies.
   *
   * @param authenticationProxy Proxy for the authentication service
   * @param investorProxy       Proxy for the investor service
   * @param orderProxy          Proxy for the order service
   * @param walletProxy         Proxy for the wallet service
   */
  public GatewayService(AuthenticationProxy authenticationProxy, InvestorProxy investorProxy,
                        OrderProxy orderProxy, WalletProxy walletProxy) {
    this.authenticationProxy = authenticationProxy;
    this.investorProxy = investorProxy;
    this.orderProxy = orderProxy;
    this.walletProxy = walletProxy;
  }

  /**
   * Get connection token from credentials
   *
   * @param credentials Credentials of the user
   * @return Connection token
   * @throws BadRequestException   when the credentials are invalid
   * @throws UnauthorizedException when the credentials are incorrect
   */
  public String connect(Credentials credentials) throws BadRequestException, UnauthorizedException {
    try {
      return authenticationProxy.connect(credentials);
    } catch (FeignException e) {
      if (e.status() == 400)
        throw new BadRequestException();
      else if (e.status() == 401)
        throw new UnauthorizedException();
      else
        throw e;
    }
  }

  /**
   * Get user pseudo from connection token
   *
   * @param token Connection token
   * @return User pseudo, or null if token invalid
   */
  public String verify(String token) {
    try {
      return authenticationProxy.verify(token);
    } catch (FeignException e) {
      if (e.status() == 401)
        return null;
      else
        throw e;
    }
  }

  /**
   * Update the credentials of the user with this username
   *
   * @param username    The username of the investor
   * @param credentials The credentials of the user
   * @throws BadRequestException If the credentials are invalid
   * @throws NotFoundException   If the user with the given username is not found
   */
  public void updateCredentials(String username, Credentials credentials)
          throws BadRequestException, NotFoundException {
    try {
      // Your implementation to update the credentials
      authenticationProxy.updateCredentials(username, credentials);
    } catch (FeignException e) {
      if (e.status() == 400) {
        throw new BadRequestException();
      } else if (e.status() == 404) {
        throw new NotFoundException();
      } else {
        // Handle other exceptions or rethrow if needed
        throw e;
      }
    }
  }

  /**
   * Get information about an investor based on the specified username.
   *
   * @param username The username of the investor to get information for
   * @return The Investor object if found, or null if the investor is not found
   * @throws UnauthorizedException if the request is unauthorized
   */
  public Investor readInvestor(String username) throws UnauthorizedException {
    try {
      return investorProxy.readInvestor(username);
    } catch (FeignException e) {
      if (e.status() == 404)
        return null;
      else if (e.status() == 400)
        throw new UnauthorizedException();
      else
        throw e;
    }
  }

  /**
   * Creates a new investor with the provided username and associated credentials.
   *
   * @param investor The InvestorWithCredentials object containing investor details and credentials
   * @throws BadRequestException if the request is malformed or invalid
   * @throws ConflictException   if a conflict occurs, such as an existing investor with the same
   *                             username
   */
  public void createInvestor(InvestorWithCredentials investor)
          throws BadRequestException, ConflictException {
    try {
      investorProxy.createInvestor(investor.getUsername(), investor.toUser());
    } catch (FeignException e) {
      if (e.status() == 400)
        throw new BadRequestException();
      else if (e.status() == 409)
        throw new ConflictException();
      else
        throw e;
    }

    try {
      authenticationProxy.createCredentials(investor.getUsername(), investor.toCredentials());
    } catch (FeignException e) {
      try {
        investorProxy.deleteInvestor(investor.getUsername());
      } catch (FeignException ignored) {
      }

      if (e.status() == 400)
        throw new BadRequestException();
      else if (e.status() == 409)
        throw new ConflictException();
      else
        throw e;
    }
  }

  /**
   * Updates an existing investor's information and credentials.
   *
   * @param investor The updated InvestorWithCredentials object
   * @throws BadRequestException if the request is malformed or invalid
   * @throws NotFoundException   if the investor to update is not found
   */
  public void updateInvestor(InvestorWithCredentials investor)
          throws BadRequestException, NotFoundException {
    Investor previousInvestor;
    try {
      previousInvestor = investorProxy.readInvestor(investor.getUsername());
    } catch (FeignException e) {
      if (e.status() == 404)
        throw new NotFoundException();
      else
        throw e;
    }

    try {
      investorProxy.updateInvestor(investor.getUsername(), investor.toUser());
    } catch (FeignException e) {
      if (e.status() == 400)
        throw new BadRequestException();
      else if (e.status() == 404)
        throw new NotFoundException();
      else
        throw e;
    }

    try {
      authenticationProxy.updateCredentials(investor.getUsername(), investor.toCredentials());
    } catch (FeignException e) {
      try {
        investorProxy.updateInvestor(investor.getUsername(), previousInvestor);
      } catch (FeignException ignored) {
      }

      if (e.status() == 400)
        throw new BadRequestException();
      else if (e.status() == 404)
        throw new NotFoundException();
      else
        throw e;
    }
  }

  /**
   * Deletes an investor, including associated orders and credentials, by the specified username.
   *
   * @param username The username of the investor to be deleted
   * @return True if the investor was found and deleted successfully, false otherwise
   */
  public boolean deleteInvestor(String username) {
    orderProxy.deleteOrdersFromInvestor(username);

    boolean found = true;
    try {
      authenticationProxy.deleteCredentials(username);
    } catch (FeignException e) {
      if (e.status() == 404)
        found = false;
      else
        throw e;
    }
    try {
      investorProxy.deleteInvestor(username);
    } catch (FeignException e) {
      if (e.status() == 404)
        found = false;
      else
        throw e;
    }
    return found;
  }

  /**
   * Place a new order
   *
   * @throws BadRequestException if the order is invalid
   */
  public void placeAnOrder() throws BadRequestException {
    Order o = new Order();
    try {
      orderProxy.makeAnOrder(o);
    } catch (FeignException e) {
      if (e.status() == 400) {
        throw new BadRequestException();
      }
    }
  }

  /**
   * @param username username of the investor
   * @return List of all orders from this user, or null if the user could not be found
   */
  public Iterable<Order> getOrderByUsername(String username)
          throws NotFoundException, UnauthorizedException {
    Investor investor;
    try {
      investor = investorProxy.readInvestor(username);
      if (!investor.getUsername().equals(username)) {
        throw new UnauthorizedException();
      }
    } catch (FeignException e) {
      if (e.status() == 404) {
        throw new NotFoundException();
      }
    }
    return orderProxy.getOrdersFromInvestor(username);
  }


  /**
   *
   * @param username username of the investor
   * @return the open positions if there are any
   * @throws UnauthorizedException if the investor doesn't have the required permissions
   */
  public Iterable<Wallet> getOpenPositions(String username) throws NotFoundException, UnauthorizedException {
    Investor investor = readInvestor(username);
    if (investor == null) {
      throw new NotFoundException();
    }
    return walletProxy.getOpenPositions(username);
  }

  /**
   * Update the cash wallet for the investor.
   *
   * @param username The username of the investor
   * @param cash   The cash to update the cash wallet
   * @return The updated cash wallet
   * @throws NotFoundException     if the investor is not found
   * @throws UnauthorizedException if the investor doesn't have the required permissions
   */
  public Iterable<Wallet> updateCashWallet(String username, double cash) throws NotFoundException, UnauthorizedException {
    try {
      Investor investor = readInvestor(username);
      if (investor == null) {
        throw new NotFoundException();
      }
    } catch (FeignException e) {
      if (e.status() == 404) {
        throw new NotFoundException();
      }
    }
    return walletProxy.updateCashWallet(username, cash);
  }

  /**
   * Get the total value of the wallet for the investor.
   *
   * @param username The username of the investor
   * @return The total value of the wallet
   * @throws NotFoundException     if the investor is not found
   * @throws UnauthorizedException if the investor doesn't have the required permissions
   */
  public double walletValue(String username) throws UnauthorizedException, NotFoundException {
    try {
      Investor investor = readInvestor(username);
      if (investor == null) {
        throw new NotFoundException();
      }
    } catch (FeignException e) {
      if (e.status() == 404) {
        throw new NotFoundException();
      }
    }
    return walletProxy.walletValue(username);
  }

  /**
   * Update the title of the investor with the specified ticker.
   *
   * @param username The username of the investor
   * @param ticker   The new ticker to update the title
   * @return The updated title value
   * @throws NotFoundException     if the investor is not found
   * @throws UnauthorizedException if the investor doesn't have the required permissions
   */
  public double updateTitle(String username, String ticker, double quantity) throws UnauthorizedException, NotFoundException {
    try {
      Investor investor = readInvestor(username);
      if (investor == null) {
        throw new NotFoundException();
      }
    } catch (FeignException e) {
      if (e.status() == 404) {
        throw new NotFoundException();
      }
    }
    return walletProxy.updateTitle(username, ticker, quantity);
  }
}
