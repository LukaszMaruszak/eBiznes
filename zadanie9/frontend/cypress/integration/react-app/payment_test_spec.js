describe('Payment', function() {
    beforeEach(() => {
        cy.visit('http://localhost:3000');
        cy.get('.add-product-button').first().click();
        cy.get('.shopping-cart').click();
        cy.get('.shopping-cart__order-button button').click();
    });

    it('Should have text', function() {
        cy.get('.payment h2').should('have.text', 'Płatność');
    });

    it('Should have button', function() {
        cy.get('.payment button').should('have.text', 'Zapłać i zamów');
    });

    it('Button should not be disabled', function() {
        cy.get('.payment button').should('not.be.disabled')
    });

    it('Form should contain 4 inputs', function() {
        cy.get('.payment__form input').should('have.length', 4)
    });

    it('User can type values in form', function() {
        cy.get('.payment__form input').eq(0).type('Test Name').should('have.value', 'Test Name');
        cy.get('.payment__form input').eq(1).type('1234123412').should('have.value', '1234123412');
        cy.get('.payment__form input').eq(2).type('02/22').should('have.value', '02/22');
        cy.get('.payment__form input').eq(3).type('123').should('have.value', '123');
    });

    it('Empty Form should be invalid', function() {
        cy.get('.payment__form input:invalid').should('have.length', 4);
    });

    it('Form should be valid after providing data', function() {
        cy.get('.payment__form input').eq(0).type('Test Name').should('have.value', 'Test Name');
        cy.get('.payment__form input').eq(1).type('1234123412').should('have.value', '1234123412');
        cy.get('.payment__form input').eq(2).type('02/22').should('have.value', '02/22');
        cy.get('.payment__form input').eq(3).type('123').should('have.value', '123');

        cy.get('.payment__form input:valid').should('have.length', 4);
    });

    it('Valid Form should show congratulation message', function() {
        cy.get('.payment__form input').eq(0).type('Test Name').should('have.value', 'Test Name');
        cy.get('.payment__form input').eq(1).type('1234123412').should('have.value', '1234123412');
        cy.get('.payment__form input').eq(2).type('02/22').should('have.value', '02/22');
        cy.get('.payment__form input').eq(3).type('123').should('have.value', '123');

        cy.get('.payment__form input:valid').should('have.length', 4);
        cy.get('.payment button').click();

        cy.get('.valid-payment h2').should('have.text', 'Gratulacje płatność przbiegła poprawnie 🎉')
    });
})
